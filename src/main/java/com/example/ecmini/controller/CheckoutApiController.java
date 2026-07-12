package com.example.ecmini.controller;

import com.example.ecmini.dto.CheckoutRequest;
import com.example.ecmini.service.CheckoutService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ecmini.entity.Order;
import com.example.ecmini.exception.PaymentException;
import com.example.ecmini.service.StripeService;
import com.stripe.model.checkout.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutApiController {

    private final CheckoutService checkoutService;
    private final StripeService stripeService;

    @PostMapping
    public ResponseEntity<Map<String, String>> checkout(
            @RequestBody CheckoutRequest request,
            HttpSession session) {

        String checkoutUrl =
                checkoutService.createCheckoutSession(
                        request,
                        session
                );

        return ResponseEntity.ok(
                Map.of("checkoutUrl", checkoutUrl)
        );
    }
    @GetMapping("/success")
    public ResponseEntity<Map<String, Object>> success(
            @RequestParam("session_id") String sessionId,
            Principal principal,
            HttpSession session) {

        Session stripeSession =
                stripeService.getSession(sessionId);

        if (!"paid".equals(stripeSession.getPaymentStatus())) {
            throw new PaymentException("決済が完了していません");
        }

        Order order = checkoutService.handleSuccess(
                principal.getName(),
                sessionId,
                session
        );

        return ResponseEntity.ok(
                Map.of(
                        "orderId", order.getId(),
                        "status", order.getStatus(),
                        "totalPrice", order.getTotalPrice()
                )
        );
    }





}