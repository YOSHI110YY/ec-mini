package com.example.ecmini.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;


@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 商品ID（Product への参照は持たない）
    private Long productId;

    // 商品名
    private String productName;

    // 価格
    private int price;

    // 数量
    private int quantity;

    // 小計
    private int subtotal;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
