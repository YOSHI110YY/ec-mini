package com.example.ecmini.controller;

import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartService;
import com.example.ecmini.entity.Order;
import com.example.ecmini.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/orders/confirm")
    public String confirmOrder(
            Authentication auth,
            Model model,
            HttpSession session) {

        Cart cart = cartService.getCart(session);

        model.addAttribute("items", cart.getItems());
        model.addAttribute("total", cart.getTotalPrice());

        return "orders/confirm";
    }

    @GetMapping("/orders")
    public String orderList(
            Authentication auth,
            Model model) {

        String username = auth.getName();
        List<Order> orders =
                orderService.getOrdersByUser(username);

        model.addAttribute("orders", orders);

        return "orders/list";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(
            @PathVariable("id") Long id,
            Authentication auth,
            Model model) {

        String username = auth.getName();

        Order order =
                orderService.getOrderByIdAndUser(
                        id,
                        username
                );

        if (order == null) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);

        return "orders/detail";
    }
}