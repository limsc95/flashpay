package com.flashpay.orderapi.product.dto;

import java.time.LocalDateTime;

import com.flashpay.orderapi.product.domain.Product;
import com.flashpay.orderapi.product.domain.ProductSaleStatus;

public record ProductResponse(
        Long id,
        String name,
        long price,
        int stockQuantity,
        LocalDateTime saleStartAt,
        ProductSaleStatus saleStatus
) {

    public static ProductResponse of(Product product, LocalDateTime now) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getSaleStartAt(),
                product.saleStatus(now)
        );
    }
}
