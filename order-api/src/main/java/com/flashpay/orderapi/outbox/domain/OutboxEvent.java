package com.flashpay.orderapi.outbox.domain;

import java.time.LocalDateTime;

import com.flashpay.orderapi.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 상태 변경과 같은 트랜잭션에서 저장하고, Outbox Relay가 PENDING을 읽어 Kafka로 발행한다.
 * 여러 종류의 이벤트를 담기 때문에 orders에 FK를 걸지 않고 aggregate_id로만 가리킨다.
 */
@Entity
@Table(name = "outbox_event", indexes = @Index(name = "idx_outbox_status_id", columnList = "status, id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String aggregateType;

    @Column(nullable = false)
    private Long aggregateId;

    @Column(nullable = false, length = 50)
    private String eventType;

    @Column(nullable = false, columnDefinition = "json")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OutboxStatus status;

    private LocalDateTime sentAt;

    private OutboxEvent(String aggregateType, Long aggregateId, String eventType, String payload) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = OutboxStatus.PENDING;
    }

    public static OutboxEvent pending(String aggregateType, Long aggregateId, String eventType, String payload) {
        return new OutboxEvent(aggregateType, aggregateId, eventType, payload);
    }

    public void markSent(LocalDateTime sentAt) {
        this.status = OutboxStatus.SENT;
        this.sentAt = sentAt;
    }
}
