package com.flashpay.orderapi.payment.domain;

public enum PaymentStatus {
    READY,      // 결제 요청 전
    DONE,       // 승인 완료
    FAILED,     // 승인 실패
    CANCELED    // 승인 후 취소
}
