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
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/orders/confirm")
    public String confirmOrder(Authentication auth, Model model, HttpSession session) {

        Cart cart = cartService.getCart(session);

        model.addAttribute("items", cart.getItems());
        model.addAttribute("total", cart.getTotalPrice());

        return "order/confirm";
    }

    @GetMapping("/orders")
    public String orderList(Authentication auth, Model model) {

        String username = auth.getName();
        List<Order> orders = orderService.getOrdersByUser(username);

        model.addAttribute("orders", orders);

        return "order/list";
    }


    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable("id") Long id,
                              Authentication auth,
                              Model model) {

        String username = auth.getName();
        Order order = orderService.getOrderByIdAndUser(id, username);

        // ★追加：orderがnull（データなし）だったら、注文一覧に戻す
        if (order == null) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "order/detail";
    }
    @PostMapping("/orders/complete")
    public String completeOrder(HttpSession session, Model model, Authentication auth) {

        Cart cart = cartService.getCart(session);
        String username = auth.getName();

        // 注文作成（在庫減算もここで実行）
        Order order = orderService.createOrder(cart, username);

        // カートを空にする
        session.removeAttribute("cart");

        // 完了画面へ渡す
        model.addAttribute("order", order);

        return "order/complete";
    }

}