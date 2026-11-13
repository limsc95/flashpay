package com.flashpay.orderapi.outbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flashpay.orderapi.outbox.domain.OutboxEvent;
import com.flashpay.orderapi.outbox.domain.OutboxStatus;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop100ByStatusOrderByIdAsc(OutboxStatus status);
}
