package com.flashpay.orderapi.product.service;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flashpay.orderapi.global.exception.BusinessException;
import com.flashpay.orderapi.global.exception.ErrorCode;
import com.flashpay.orderapi.product.dto.ProductPageResponse;
import com.flashpay.orderapi.product.dto.ProductResponse;
import com.flashpay.orderapi.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final Clock clock;

    public ProductPageResponse getProducts(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now(clock);
        return ProductPageResponse.from(
                productRepository.findAll(pageable).map(product -> ProductResponse.of(product, now))
        );
    }

    public ProductResponse getProduct(Long productId) {
        return productRepository.findById(productId)
                .map(product -> ProductResponse.of(product, LocalDateTime.now(clock)))
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
