package com.example.ecmini.controller;

import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Test
    void カート画面を表示できる() {
        MockHttpSession session = new MockHttpSession();
        Model model = new ConcurrentModel();
        Cart cart = new Cart();

        when(cartService.getCart(session)).thenReturn(cart);

        String viewName = cartController.showCart(model, session);

        assertEquals("cart/list", viewName);
        assertSame(cart, model.getAttribute("cart"));
        verify(cartService).getCart(session);
    }

    @Test
    void 商品をカートに追加できる() {
        MockHttpSession session = new MockHttpSession();

        String viewName = cartController.addToCart(1L, session);

        assertEquals("redirect:/cart", viewName);
        verify(cartService).addToCart(1L, session);
    }

    @Test
    void カート商品の数量を更新できる() {
        MockHttpSession session = new MockHttpSession();

        String viewName = cartController.updateQuantity(1L, 3, session);

        assertEquals("redirect:/cart", viewName);
        verify(cartService).updateQuantity(1L, 3, session);
    }

    @Test
    void カート商品を削除できる() {
        MockHttpSession session = new MockHttpSession();

        String viewName = cartController.deleteItem(1L, session);

        assertEquals("redirect:/cart", viewName);
        verify(cartService).removeItem(1L, session);
    }
}