package com.example.ecmini.controller;

import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartService;
import com.example.ecmini.entity.Order;
import com.example.ecmini.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private CartService cartService;

    @Mock
    private Authentication auth;

    @InjectMocks
    private OrderController orderController;

    @Test
    void 注文一覧画面を表示できる() {
        String username = "testuser";
        Order order = new Order();
        List<Order> orders = List.of(order);

        when(auth.getName()).thenReturn(username);
        when(orderService.getOrdersByUser(username)).thenReturn(orders);

        Model model = new ConcurrentModel();

        String viewName = orderController.orderList(auth, model);

        assertEquals("orders/list", viewName);
        assertSame(orders, model.getAttribute("orders"));

        verify(auth).getName();
        verify(orderService).getOrdersByUser(username);
    }

    @Test
    void 注文詳細画面を表示できる() {
        Long orderId = 1L;
        String username = "testuser";
        Order order = new Order();

        when(auth.getName()).thenReturn(username);
        when(orderService.getOrderByIdAndUser(orderId, username)).thenReturn(order);

        Model model = new ConcurrentModel();

        String viewName = orderController.orderDetail(orderId, auth, model);

        assertEquals("orders/detail", viewName);
        assertSame(order, model.getAttribute("order"));

        verify(auth).getName();
        verify(orderService).getOrderByIdAndUser(orderId, username);
    }
    @Test
    void 注文確認画面を表示できる() {
        MockHttpSession session = new MockHttpSession();
        Model model = new ConcurrentModel();

        Cart cart = mock(Cart.class);

        when(cartService.getCart(session)).thenReturn(cart);
        when(cart.getItems()).thenReturn(List.of());
        when(cart.getTotalPrice()).thenReturn(0);

        String viewName = orderController.confirmOrder(auth, model, session);

        assertEquals("orders/confirm", viewName);
        assertEquals(List.of(), model.getAttribute("items"));
        assertEquals(0, model.getAttribute("total"));

        verify(cartService).getCart(session);
        verify(cart).getItems();
        verify(cart).getTotalPrice();
    }
    @Test
    void 注文完了処理ができる() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", "dummy");

        Model model = new ConcurrentModel();

        Cart cart = new Cart();
        Order order = new Order();
        String username = "testuser";

        when(cartService.getCart(session)).thenReturn(cart);
        when(auth.getName()).thenReturn(username);
        when(orderService.createOrder(cart, username)).thenReturn(order);

        String viewName = orderController.completeOrder(session, model, auth);

        assertEquals("orders/complete", viewName);
        assertSame(order, model.getAttribute("order"));
        assertNull(session.getAttribute("cart"));

        verify(cartService).getCart(session);
        verify(auth).getName();
        verify(orderService).createOrder(cart, username);
    }

}