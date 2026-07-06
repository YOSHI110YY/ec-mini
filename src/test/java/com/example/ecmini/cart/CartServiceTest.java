package com.example.ecmini.cart;

import com.example.ecmini.entity.Product;
import com.example.ecmini.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    @Test
    void getCart_セッションにカートがない場合は新規作成して返す() {
        when(session.getAttribute("cart")).thenReturn(null);

        Cart cart = cartService.getCart(session);

        assertEquals(0, cart.getItems().size());
        verify(session).setAttribute(eq("cart"), any(Cart.class));
    }

    @Test
    void getCart_セッションにカートがある場合は既存カートを返す() {
        Cart existingCart = new Cart();

        when(session.getAttribute("cart")).thenReturn(existingCart);

        Cart cart = cartService.getCart(session);

        assertEquals(existingCart, cart);
    }

    @Test
    void addToCart_同じ商品を追加すると数量が増える() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");
        product.setPrice(1000);
        product.setStock(5);

        Cart cart = new Cart();
        cart.addItem(new CartItem(product));

        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(product);

        cartService.addToCart(1L, session);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }
    @Test
    void addToCart_同じ商品を在庫数を超えて追加すると例外() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");
        product.setPrice(1000);
        product.setStock(1);

        Cart cart = new Cart();
        cart.addItem(new CartItem(product));

        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(product);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.addToCart(1L, session));

        assertEquals("在庫が不足しています", exception.getMessage());
        assertEquals(1, cart.getItems().get(0).getQuantity());
    }

    @Test
    void updateQuantity_数量を変更できる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");
        product.setPrice(1000);
        product.setStock(5);

        Cart cart = new Cart();
        cart.addItem(new CartItem(product));

        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(product);

        cartService.updateQuantity(1L, 3, session);

        assertEquals(3, cart.getItems().get(0).getQuantity());
    }

    @Test
    void removeItem_商品を削除できる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");
        product.setPrice(1000);
        product.setStock(5);

        Cart cart = new Cart();
        cart.addItem(new CartItem(product));

        when(session.getAttribute("cart")).thenReturn(cart);

        cartService.removeItem(1L, session);

        assertEquals(0, cart.getItems().size());
    }

    @Test
    void updateQuantity_在庫数を超える数量を指定した場合は在庫数に変更される() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");
        product.setPrice(1000);
        product.setStock(5);

        Cart cart = new Cart();
        cart.addItem(new CartItem(product));

        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(product);

        cartService.updateQuantity(1L, 10, session);

        assertEquals(5, cart.getItems().get(0).getQuantity());
    }
    @Test
    void updateQuantity_数量0以下なら商品が削除される() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");
        product.setPrice(1000);
        product.setStock(5);

        Cart cart = new Cart();
        cart.addItem(new CartItem(product));

        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.findById(1L)).thenReturn(product);

        cartService.updateQuantity(1L, 0, session);

        assertEquals(0, cart.getItems().size());
    }


}