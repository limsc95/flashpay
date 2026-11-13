package com.flashpay.orderapi.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ProductTest {

    private final LocalDateTime saleStartAt = LocalDateTime.of(2026, 11, 20, 10, 0);

    @Test
    void 판매_시작_전이면_UPCOMING() {
        Product product = Product.create("키보드", 10_000, 10, saleStartAt);

        assertThat(product.saleStatus(saleStartAt.minusSeconds(1))).isEqualTo(ProductSaleStatus.UPCOMING);
    }

    @Test
    void 판매_시작_시각부터_ON_SALE() {
        Product product = Product.create("키보드", 10_000, 10, saleStartAt);

        assertThat(product.saleStatus(saleStartAt)).isEqualTo(ProductSaleStatus.ON_SALE);
    }

    @Test
    void 재고가_0이면_SOLD_OUT() {
        Product product = Product.create("키보드", 10_000, 0, saleStartAt);

        assertThat(product.saleStatus(saleStartAt.plusHours(1))).isEqualTo(ProductSaleStatus.SOLD_OUT);
    }

    @Test
    void 가격이_음수면_생성할_수_없다() {
        assertThatThrownBy(() -> Product.create("키보드", -1, 10, saleStartAt))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
