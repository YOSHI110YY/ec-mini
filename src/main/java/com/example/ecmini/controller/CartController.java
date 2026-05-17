package com.example.ecmini.controller;

import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        Cart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        return "cart/list";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        cartService.addToCart(id, session);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateQuantity(
            @RequestParam Long productId,
            @RequestParam int quantity,
            HttpSession session
    ) {
        cartService.updateQuantity(productId, quantity, session);
        return "redirect:/cart";
    }

    @PostMapping("/cart/delete/{id}")
    public String deleteItem(@PathVariable Long id, HttpSession session) {
        cartService.removeItem(id, session);
        return "redirect:/cart";
    }
}
