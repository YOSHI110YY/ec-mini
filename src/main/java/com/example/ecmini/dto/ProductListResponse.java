package com.example.ecmini.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductListResponse {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
}