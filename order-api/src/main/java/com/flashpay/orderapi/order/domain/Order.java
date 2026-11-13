package com.flashpay.orderapi.order.domain;

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
 * 회원과 상품은 객체 연관관계 대신 ID로만 참조한다.
 * 주문 처리에 회원, 상품 엔티티 전체가 필요하지 않고, 나중에 서비스를 나눌 때 경계가 깔끔해진다.
 */
@Entity
@Table(name = "orders", uniqueConstraints = @UniqueConstraint(name = "uk_orders_idempotency_key", columnNames = "idempotency_key"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    /** 주문 시점의 총 결제 금액(원). 상품 가격이 나중에 바뀌어도 주문 금액은 그대로 남는다. */
    @Column(nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false, length = 64)
    private String idempotencyKey;

    private Order(Long memberId, Long productId, int quantity, long amount, String idempotencyKey) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
        this.idempotencyKey = idempotencyKey;
        this.status = OrderStatus.PENDING;
    }

    public static Order create(Long memberId, Long productId, int quantity, long unitPrice, String idempotencyKey) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("주문 수량은 1 이상이어야 합니다.");
        }
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("멱등성 키가 필요합니다.");
        }
        long amount = Math.multiplyExact(unitPrice, quantity);
        return new Order(memberId, productId, quantity, amount, idempotencyKey);
    }

    public void markPaid() {
        requireStatus(OrderStatus.PENDING);
        this.status = OrderStatus.PAID;
    }

    public void markFailed() {
        requireStatus(OrderStatus.PENDING);
        this.status = OrderStatus.FAILED;
    }

    public void cancel() {
        requireStatus(OrderStatus.PAID);
        this.status = OrderStatus.CANCELED;
    }

    private void requireStatus(OrderStatus expected) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    "주문 상태가 " + expected + "일 때만 가능합니다. 현재 상태: " + this.status);
        }
    }
}
