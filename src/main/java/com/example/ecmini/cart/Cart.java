package com.example.ecmini.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void removeItem(Long productId) {
        items.removeIf(i -> i.getProductId().equals(productId));
    }

    public int getTotalPrice() {
        return items.stream()
                .mapToInt(CartItem::getSubtotal)
                .sum();
    }
}
