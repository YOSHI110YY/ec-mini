package com.example.ecmini.controller;

import com.example.ecmini.entity.Order;
import com.example.ecmini.exception.PaymentException;
import com.example.ecmini.service.CheckoutService;
import com.example.ecmini.service.StripeService;
import com.stripe.model.checkout.Session;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final StripeService stripeService;

    @PostMapping
    public String checkout(HttpSession session) {

        System.out.println("=== POST /checkout に到達 ===");

        String url =
                checkoutService.createCheckoutSession(session);

        System.out.println("=== Stripe URL: " + url + " ===");

        return "redirect:" + url;
    }

    @GetMapping("/success")
    public String success(
            @RequestParam("session_id") String sessionId,
            Principal principal,
            HttpSession session,
            Model model) {

        Session stripeSession =
                stripeService.getSession(sessionId);

        if (!"paid".equals(stripeSession.getPaymentStatus())) {
            throw new PaymentException("決済が完了していません");
        }

        Order order =
                checkoutService.handleSuccess(
                        principal.getName(),
                        sessionId,
                        session
                );

        model.addAttribute("order", order);

        return "orders/complete";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "orders/cancel";
    }
}