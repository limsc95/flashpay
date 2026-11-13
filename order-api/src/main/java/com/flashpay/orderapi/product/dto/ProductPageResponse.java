package com.flashpay.orderapi.product.dto;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Page 객체를 그대로 JSON으로 내보내면 내부 구조가 노출되고 버전마다 형식이 바뀔 수 있어서
 * 필요한 필드만 담은 응답 DTO로 감싼다.
 */
public record ProductPageResponse(
        List<ProductResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {

    public static ProductPageResponse from(Page<ProductResponse> page) {
        return new ProductPageResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }
}
