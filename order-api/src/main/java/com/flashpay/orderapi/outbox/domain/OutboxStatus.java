package com.flashpay.orderapi.outbox.domain;

public enum OutboxStatus {
    PENDING,    // 발행 대기
    SENT        // Kafka 발행 완료
}
