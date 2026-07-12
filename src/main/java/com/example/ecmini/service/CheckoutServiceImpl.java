package com.example.ecmini.service;

import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartService;
import com.example.ecmini.entity.Order;
import com.example.ecmini.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ecmini.cart.CartItem;
import com.example.ecmini.dto.CheckoutItemRequest;
import com.example.ecmini.dto.CheckoutRequest;
import com.example.ecmini.entity.Product;
import com.example.ecmini.exception.PaymentException;
import com.example.ecmini.exception.StockException;


@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CartService cartService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final StripeService stripeService;
    private final ProductService productService;

    // Thymeleaf用
    @Override
    public String createCheckoutSession(HttpSession session) {
        Cart cart = cartService.getCart(session);
        return stripeService.createCheckoutSession(cart);
    }

    // React用
    @Override
    public String createCheckoutSession(
            CheckoutRequest request,
            HttpSession session) {

        if (request == null
                || request.getItems() == null
                || request.getItems().isEmpty()) {
            throw new PaymentException("カートに商品がありません");
        }

        Cart cart = new Cart();

        for (CheckoutItemRequest requestItem : request.getItems()) {

            if (requestItem.getQuantity() <= 0) {
                throw new PaymentException("商品の数量が不正です");
            }

            Product product =
                    productService.findById(requestItem.getProductId());

            if (product.getStock() < requestItem.getQuantity()) {
                throw new StockException(
                        "在庫が不足しています: " + product.getName()
                );
            }

            CartItem cartItem = new CartItem(product);
            cartItem.setQuantity(requestItem.getQuantity());

            cart.addItem(cartItem);
        }

        session.setAttribute("cart", cart);

        return stripeService.createCheckoutSession(cart);
    }

    @Transactional
    @Override
    public Order handleSuccess(
            String username,
            String sessionId,
            HttpSession session) {

        return orderRepository.findByStripeSessionId(sessionId)
                .orElseGet(() -> {
                    Cart cart = cartService.getCart(session);

                    Order order = orderService.createOrder(
                            cart,
                            username,
                            sessionId
                    );

                    cartService.clearCart(session);

                    return order;
                });
    }
}