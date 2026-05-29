package com.example.ecmini.service;

import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartItem;
import com.example.ecmini.entity.Order;
import com.example.ecmini.entity.Product;
import com.example.ecmini.repository.OrderItemRepository;
import com.example.ecmini.repository.OrderRepository;
import com.example.ecmini.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_注文を作成し在庫を減らす() {
        Product product = new Product();
        product.setId(1L);
        product.setName("チキン弁当");
        product.setPrice(800);
        product.setStock(10);
        product.setImage("chicken.jpg");

        Cart cart = new Cart();
        CartItem item = new CartItem(product);
        item.setQuantity(2);
        cart.getItems().add(item);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Order order = orderService.createOrder(cart, "testuser");

        assertEquals("testuser", order.getUsername());
        assertEquals("ORDERED", order.getStatus());
        assertEquals(1600, order.getTotalPrice());
        assertEquals(8, product.getStock());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).save(product);
        verify(orderItemRepository, times(1)).save(any());
    }

    @Test
    void createOrder_商品が存在しない場合は例外() {
        Product product = new Product();
        product.setId(999L);
        product.setName("存在しない商品");
        product.setPrice(1000);
        product.setStock(10);
        product.setImage("dummy.jpg");

        Cart cart = new Cart();
        CartItem item = new CartItem(product);
        item.setQuantity(1);
        cart.getItems().add(item);

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(cart, "testuser"));

        assertTrue(exception.getMessage().contains("商品が見つかりません"));
    }

    @Test
    void createOrder_在庫不足の場合は例外() {
        Product product = new Product();
        product.setId(1L);
        product.setName("チキン弁当");
        product.setPrice(800);
        product.setStock(2);
        product.setImage("chicken.jpg");

        Cart cart = new Cart();
        CartItem item = new CartItem(product);
        item.setQuantity(5);
        cart.getItems().add(item);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(cart, "testuser"));

        assertTrue(exception.getMessage().contains("在庫が不足しています"));
    }

    @Test
    void getOrdersByUser_ユーザー名で注文一覧を取得する() {
        Order order = new Order();
        order.setUsername("testuser");

        when(orderRepository.findByUsername("testuser")).thenReturn(List.of(order));

        List<Order> orders = orderService.getOrdersByUser("testuser");

        assertEquals(1, orders.size());
        assertEquals("testuser", orders.get(0).getUsername());
    }

    @Test
    void findById_注文が存在する場合は取得する() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void updateStatus_注文ステータスを更新する() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("ORDERED");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.updateStatus(1L, "SHIPPED");

        assertEquals("SHIPPED", order.getStatus());
    }
}