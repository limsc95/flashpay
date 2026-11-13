package com.flashpay.orderapi.payment.domain;

import java.time.LocalDateTime;

import com.flashpay.orderapi.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * order_id에 유니크 제약을 걸어 주문 하나에 결제가 두 번 생기지 않게 막는다.
 */
@Entity
@Table(name = "payment", uniqueConstraints = @UniqueConstraint(name = "uk_payment_order_id", columnNames = "order_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    /** 정산 배치가 이 결제를 정산에 포함하면 채워진다. */
    private Long settlementId;

    /** PG사가 발급한 거래 키. 승인 전에는 비어 있다. */
    @Column(length = 200)
    private String paymentKey;

    @Column(nullable = false)
    private long amount;

    @Column(length = 30)
    private String method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    private LocalDateTime approvedAt;

    private Payment(Long orderId, long amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.READY;
    }

    public static Payment ready(Long orderId, long amount) {
        return new Payment(orderId, amount);
    }

    public void approve(String paymentKey, String method, LocalDateTime approvedAt) {
        requireStatus(PaymentStatus.READY);
        this.paymentKey = paymentKey;
        this.method = method;
        this.approvedAt = approvedAt;
        this.status = PaymentStatus.DONE;
    }

    public void fail() {
        requireStatus(PaymentStatus.READY);
        this.status = PaymentStatus.FAILED;
    }

    public void cancel() {
        requireStatus(PaymentStatus.DONE);
        this.status = PaymentStatus.CANCELED;
    }

    private void requireStatus(PaymentStatus expected) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    "결제 상태가 " + expected + "일 때만 가능합니다. 현재 상태: " + this.status);
        }
    }
}
