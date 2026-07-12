package com.example.ecmini.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutItemRequest {

    private Long productId;
    private int quantity;
}