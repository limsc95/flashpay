package com.flashpay.orderapi.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 주문을_만들면_PENDING이고_금액은_단가_곱하기_수량() {
        Order order = Order.create(1L, 1L, 3, 10_000, "key-1");

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getAmount()).isEqualTo(30_000);
    }

    @Test
    void 결제_완료된_주문은_실패로_바꿀_수_없다() {
        Order order = Order.create(1L, 1L, 1, 10_000, "key-1");
        order.markPaid();

        assertThatThrownBy(order::markFailed).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 결제_전_주문은_취소할_수_없다() {
        Order order = Order.create(1L, 1L, 1, 10_000, "key-1");

        assertThatThrownBy(order::cancel).isInstanceOf(IllegalStateException.class);
    }
}
