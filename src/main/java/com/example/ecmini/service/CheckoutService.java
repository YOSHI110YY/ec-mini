package com.example.ecmini.service;

import com.example.ecmini.dto.CheckoutRequest;
import com.example.ecmini.entity.Order;
import jakarta.servlet.http.HttpSession;

public interface CheckoutService {

    String createCheckoutSession(HttpSession session);

    String createCheckoutSession(
            CheckoutRequest request,
            HttpSession session
    );

    Order handleSuccess(
            String username,
            String sessionId,
            HttpSession session
    );
}