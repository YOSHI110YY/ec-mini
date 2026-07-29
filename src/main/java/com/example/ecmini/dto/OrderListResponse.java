package com.example.ecmini.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderListResponse {

    private Long id;
    private Integer totalPrice;
    private String status;
    private LocalDateTime createdAt;
}