package com.example.ecmini.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private String status;
    private LocalDateTime createdAt;
}