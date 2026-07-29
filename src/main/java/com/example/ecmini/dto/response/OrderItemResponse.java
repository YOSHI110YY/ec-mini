package com.example.ecmini.dto.response;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        Integer price,
        Integer quantity,
        Integer subtotal) {
}