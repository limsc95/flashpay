package com.flashpay.orderapi.global.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

/**
 * 에러 응답은 RFC 9457 표준 형식인 ProblemDetail로 통일한다.
 * 예: {"type":"about:blank","title":"PRODUCT_NOT_FOUND","status":404,"detail":"상품을 찾을 수 없습니다.","code":"PRODUCT_NOT_FOUND"}
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException e) {
        log.info("business exception: code={}, message={}", e.getErrorCode(), e.getMessage());
        return toProblemDetail(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return toProblemDetail(ErrorCode.INVALID_INPUT, e.getName() + " 값이 올바르지 않습니다.");
    }

    private ProblemDetail toProblemDetail(ErrorCode errorCode, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(errorCode.getStatus(), detail);
        problemDetail.setTitle(errorCode.name());
        problemDetail.setProperty("code", errorCode.name());
        return problemDetail;
    }
}
