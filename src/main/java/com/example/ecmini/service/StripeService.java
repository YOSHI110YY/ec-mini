package com.example.ecmini.service;

import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartItem;
import com.example.ecmini.exception.PaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class StripeService {

    public StripeService(
            @Value("${stripe.secret-key}") String secretKey
    ) {
        Stripe.apiKey = secretKey;
    }

    public String createCheckoutSession(Cart cart) {

        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(
                                "http://localhost:5173/order/complete"
                                        + "?session_id={CHECKOUT_SESSION_ID}"
                        )
                        .setCancelUrl(
                                "http://localhost:5173/order/cancel"
                        );
        for (CartItem item : cart.getItems()) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("jpy")
                                            .setUnitAmount((long) item.getPrice())
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getName())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }
        try {
            Session session = Session.create(paramsBuilder.build());

            return session.getUrl();

        } catch (StripeException e) {
            throw new PaymentException("決済ページの作成に失敗しました"+ e.getMessage());
        }
    }
    public Session getSession(String sessionId) {

        try {
            return Session.retrieve(sessionId);

        } catch (StripeException e) {
            throw new PaymentException(
                    "決済情報の取得に失敗しました: " + e.getMessage()
            );
        }
    }
}
