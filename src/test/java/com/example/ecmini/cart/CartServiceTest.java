package com.example.ecmini.cart;

import com.example.ecmini.entity.Product;
import com.example.ecmini.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartService cartService;

    @Test
    void addToCart_在庫がある商品をカートに追加できる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");
        product.setPrice(1000);
        product.setStock(5);

        Cart cart = new Cart();

        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(product);

        cartService.addToCart(1L, session);

        assertEquals(1, cart.getItems().size());
        assertEquals(1L, cart.getItems().get(0).getProductId());
        assertEquals(1, cart.getItems().get(0).getQuantity());
    }

    @Test
    void addToCart_在庫がない商品は追加できない() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");
        product.setPrice(1000);
        product.setStock(0);

        Cart cart = new Cart();

        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(product);

        assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(1L, session);
        });
    }
}