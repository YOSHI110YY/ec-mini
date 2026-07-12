package com.example.ecmini.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutRequest {

    private List<CheckoutItemRequest> items;
}