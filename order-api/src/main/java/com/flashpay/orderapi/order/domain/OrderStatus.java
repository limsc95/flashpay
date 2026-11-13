package com.flashpay.orderapi.order.domain;

public enum OrderStatus {
    PENDING,    // 주문 생성, 결제 대기
    PAID,       // 결제 완료
    FAILED,     // 결제 실패
    CANCELED    // 결제 완료 후 취소
}
