package com.flashpay.orderapi.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flashpay.orderapi.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}
