package com.example.ecmini.service;

import com.example.ecmini.exception.OrderException;
import com.example.ecmini.exception.StockException;
import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartItem;
import com.example.ecmini.entity.Order;
import com.example.ecmini.entity.OrderItem;
import com.example.ecmini.entity.Product;
import com.example.ecmini.repository.OrderItemRepository;
import com.example.ecmini.repository.OrderRepository;
import com.example.ecmini.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(Cart cart, String username) {
        return createOrder(cart, username, null);
    }

    @Transactional
    public Order createOrder(
            Cart cart,
            String username,
            String stripeSessionId) {

        if (stripeSessionId != null
                && orderRepository.existsByStripeSessionId(stripeSessionId)) {
            throw new OrderException("この決済はすでに注文処理されています");
        }

        if (cart.getItems().isEmpty()) {
            throw new OrderException("カートが空です");
        }

        for (CartItem ci : cart.getItems()) {
            Product p = productRepository.findById(ci.getProductId())
                    .orElseThrow(() ->
                            new OrderException(
                                    "商品が見つかりません: " + ci.getProductId()));

            if (p.getStock() < ci.getQuantity()) {
                throw new OrderException(
                        "在庫が不足しています: " + p.getName());
            }
        }

        Order order = new Order();
        order.setUsername(username);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus("PAID");
        order.setStripeSessionId(stripeSessionId);

        orderRepository.save(order);

        for (CartItem ci : cart.getItems()) {
            Product p = productRepository.findById(ci.getProductId())
                    .orElseThrow(() ->
                            new OrderException(
                                    "商品が見つかりません: " + ci.getProductId()));

            p.setStock(p.getStock() - ci.getQuantity());
            productRepository.save(p);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProductId(ci.getProductId());
            oi.setProductName(ci.getName());
            oi.setPrice(ci.getPrice());
            oi.setQuantity(ci.getQuantity());
            oi.setSubtotal(ci.getSubtotal());

            orderItemRepository.save(oi);
        }

        return order;
    }

    public List<Order> getOrdersByUser(String username) {

        return orderRepository.findByUsername(username);
    }
    public Order getOrderByIdAndUser(Long id, String username) {
        return orderRepository.findByIdAndUsernameWithItems(id, username)
                .orElse(null);
    }

    public List<Order> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderException("注文が見つかりません: " + id));
    }
    @Transactional
    public void updateStatus(Long id, String status) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderException("注文が見つかりません: " + id));

        order.setStatus(status);

        orderRepository.save(order);
    }
}
