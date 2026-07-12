package com.example.ecmini.controller;

import com.example.ecmini.dto.OrderResponse;
import com.example.ecmini.entity.Order;
import com.example.ecmini.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderApiController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            Principal principal
    ) {
        List<OrderResponse> orders =
                orderService.getOrdersByUser(
                                principal.getName()
                        )
                        .stream()
                        .map(order -> new OrderResponse(
                                order.getId(),
                                order.getTotalPrice(),
                                order.getStatus(),
                                order.getCreatedAt()
                        ))
                        .toList();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(
            @PathVariable Long id,
            Principal principal
    ) {
        Order order = orderService.getOrderByIdAndUser(
                id,
                principal.getName()
        );

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(order);
    }
}