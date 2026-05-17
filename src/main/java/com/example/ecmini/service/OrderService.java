package com.example.ecmini.service;

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

        // 注文作成
        Order order = new Order();
        order.setUsername(username);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus("ORDERED");
        orderRepository.save(order);

        // 注文アイテム保存 & 在庫減算
        for (CartItem ci : cart.getItems()) {

            // 在庫減算
            Product p = productRepository.findById(ci.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品が見つかりません: " + ci.getProductId()));

            if (p.getStock() < ci.getQuantity()) {
                throw new RuntimeException("在庫が不足しています: " + p.getName());
            }

            p.setStock(p.getStock() - ci.getQuantity());
            productRepository.save(p);

            // 注文アイテム保存
            OrderItem oi = new OrderItem();

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
        return orderRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new RuntimeException("注文が見つかりません"));
    }
    public List<Order> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("注文が見つかりません: " + id));
    }
    @Transactional
    public void updateStatus(Long id, String status) {

        Order order = orderRepository.findById(id)
                .orElseThrow();

        order.setStatus(status);
    }

    public long getPendingShipmentCount() {
        return orderRepository.countByStatus("ORDERED");
    }


}
