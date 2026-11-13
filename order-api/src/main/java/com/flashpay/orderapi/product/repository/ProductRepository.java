package com.flashpay.orderapi.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flashpay.orderapi.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
