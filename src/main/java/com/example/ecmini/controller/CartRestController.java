package com.example.ecmini.controller;

import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    // カートに追加
    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        cartService.addToCart(id, session);
        return "OK";
    }

    // 数量変更
    @PostMapping("/update")
    public String updateQuantity(
            @RequestParam Long id,
            @RequestParam int quantity,
            HttpSession session
    ) {
        cartService.updateQuantity(id, quantity, session);
        return "OK";
    }

    // 削除
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        cartService.removeItem(id, session);
        return "OK";
    }

    // カート内容取得（必要なら）
    @GetMapping
    public Cart getCart(HttpSession session) {
        return cartService.getCart(session);
    }
}
