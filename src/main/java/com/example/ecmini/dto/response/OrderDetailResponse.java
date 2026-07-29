package com.example.ecmini.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long id,
        String username,
        int totalPrice,
        LocalDateTime createdAt,
        String status,
        List<OrderItemResponse> items) {
}