package com.example.ecmini.cart;

import com.example.ecmini.entity.Product;
import com.example.ecmini.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
@RequiredArgsConstructor
public class CartService {

    private final ProductService productService;
    private final List<CartItem> items = new ArrayList<>();

    public void addToCart(Long productId) {
        Product product = productService.findById(productId);

        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        items.add(new CartItem(product));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void remove(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public int getTotal() {
        return items.stream().mapToInt(CartItem::getSubtotal).sum();
    }
}
