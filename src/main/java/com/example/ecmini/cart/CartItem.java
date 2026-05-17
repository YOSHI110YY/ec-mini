package com.example.ecmini.cart;

import com.example.ecmini.entity.Product;

public class CartItem {

    private Long productId;
    private String name;
    private int price;
    private int quantity;
    private String image;

    public CartItem(Product product) {
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.image = product.getImage();
        this.quantity = 1; // 初期値
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public int getSubtotal() {
        return price * quantity;
    }
}
