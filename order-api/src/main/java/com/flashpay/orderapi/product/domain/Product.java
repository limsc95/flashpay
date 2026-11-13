package com.flashpay.orderapi.product.domain;

import java.time.LocalDateTime;

import com.flashpay.orderapi.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    /** 원 단위 금액. 소수점이 없는 원화라 long으로 둔다. */
    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private LocalDateTime saleStartAt;

    /** 2주차 재고 차감 동시성 비교(낙관적 락)에서 사용한다. */
    @Version
    private Long version;

    private Product(String name, long price, int stockQuantity, LocalDateTime saleStartAt) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.saleStartAt = saleStartAt;
    }

    public static Product create(String name, long price, int stockQuantity, LocalDateTime saleStartAt) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명은 비어 있을 수 없습니다.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
        }
        if (saleStartAt == null) {
            throw new IllegalArgumentException("판매 시작 시각이 필요합니다.");
        }
        return new Product(name, price, stockQuantity, saleStartAt);
    }

    public ProductSaleStatus saleStatus(LocalDateTime now) {
        if (now.isBefore(saleStartAt)) {
            return ProductSaleStatus.UPCOMING;
        }
        if (stockQuantity <= 0) {
            return ProductSaleStatus.SOLD_OUT;
        }
        return ProductSaleStatus.ON_SALE;
    }
}
