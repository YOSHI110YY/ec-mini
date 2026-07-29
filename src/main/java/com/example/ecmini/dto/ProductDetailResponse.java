package com.example.ecmini.dto;

import lombok.Data;

@Data
public class ProductDetailResponse {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private String description;
    private Integer stock;
}