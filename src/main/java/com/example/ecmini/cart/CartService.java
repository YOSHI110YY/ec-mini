package com.example.ecmini.cart;

import com.example.ecmini.cart.Cart;
import com.example.ecmini.cart.CartItem;
import com.example.ecmini.entity.Product;
import com.example.ecmini.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    // ▼ Session から Cart を取得（なければ作成）
    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // ▼ カートに追加
    public void addToCart(Long productId, HttpSession session) {

        Cart cart = getCart(session);
        Product product = productService.findById(productId);

        // 既にカートにある場合は数量+1
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {

                // 在庫チェック
                if (item.getQuantity() + 1 > product.getStock()) {
                    throw new RuntimeException("在庫が不足しています");
                }

                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        // 新規追加
        if (product.getStock() < 1) {
            throw new RuntimeException("在庫がありません");
        }

        cart.addItem(new CartItem(product));
    }

    // ▼ 数量変更
    public void updateQuantity(Long productId, int quantity, HttpSession session) {

        Cart cart = getCart(session);
        Product product = productService.findById(productId);

        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {

                if (quantity <= 0) {
                    cart.removeItem(productId);
                    return;
                }

                // 在庫チェック
                if (quantity > product.getStock()) {
                    item.setQuantity(product.getStock());
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    // ▼ 削除
    public void removeItem(Long productId, HttpSession session) {
        Cart cart = getCart(session);
        cart.removeItem(productId);
    }
}
