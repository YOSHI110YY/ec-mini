package com.example.ecmini.controller;

import com.example.ecmini.dto.OrderListResponse;
import com.example.ecmini.dto.response.OrderDetailResponse;
import com.example.ecmini.dto.response.OrderItemResponse;
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
        public ResponseEntity<List<OrderListResponse>> getOrders(
                        Principal principal) {
                List<OrderListResponse> orders = orderService.getOrdersByUser(
                                principal.getName())
                                .stream()
                                .map(order -> new OrderListResponse(
                                                order.getId(),
                                                order.getTotalPrice(),
                                                order.getStatus(),
                                                order.getCreatedAt()))
                                .toList();

                return ResponseEntity.ok(orders);
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrderDetailResponse> getOrder(
                        @PathVariable Long id,
                        Principal principal) {

                Order order = orderService.getOrderByIdAndUser(
                                id,
                                principal.getName());

                if (order == null) {
                        return ResponseEntity.notFound().build();
                }

                List<OrderItemResponse> items = order.getItems()
                                .stream()
                                .map(item -> new OrderItemResponse(
                                                item.getId(),
                                                item.getProductId(),
                                                item.getProductName(),
                                                item.getPrice(),
                                                item.getQuantity(),
                                                item.getSubtotal()))
                                .toList();

                OrderDetailResponse response = new OrderDetailResponse(
                                order.getId(),
                                order.getUsername(),
                                order.getTotalPrice(),
                                order.getCreatedAt(),
                                order.getStatus(),
                                items);

                return ResponseEntity.ok(response);
        }
}