package com.example.ecmini.controller;

import com.example.ecmini.entity.Product;
import com.example.ecmini.service.FavoriteService;
import com.example.ecmini.service.ProductService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private FavoriteService favoriteService;

    @InjectMocks
    private ProductController productController;

    @Test
    void 商品一覧画面を表示できる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("テスト商品");

        Page<Product> page = new PageImpl<>(List.of(product));

        when(productService.searchWithPaging(null, null, 0)).thenReturn(page);

        Model model = new ConcurrentModel();
        MockHttpServletRequest request = new MockHttpServletRequest();

        String viewName = productController.list(null, null, 0, model, request);

        assertEquals("products/list", viewName);
        assertTrue(model.containsAttribute("productPage"));
        assertTrue(model.containsAttribute("products"));
        assertEquals(0, model.getAttribute("currentPage"));

        verify(productService).searchWithPaging(null, null, 0);
    }

    @Test
    void 商品一覧画面で検索条件を渡せる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("チキン");

        Page<Product> page = new PageImpl<>(List.of(product));

        when(productService.searchWithPaging("チキン", "MAIN", 1)).thenReturn(page);

        Model model = new ConcurrentModel();
        MockHttpServletRequest request = new MockHttpServletRequest();

        String viewName = productController.list("チキン", "MAIN", 1, model, request);

        assertEquals("products/list", viewName);
        assertEquals("チキン", model.getAttribute("name"));
        assertEquals("MAIN", model.getAttribute("category"));
        assertEquals(1, model.getAttribute("currentPage"));

        verify(productService).searchWithPaging("チキン", "MAIN", 1);
    }

    @Test
    void 商品詳細画面を表示できる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("テスト商品");
        product.setCategory("MAIN");

        Product relatedProduct = new Product();
        relatedProduct.setId(2L);
        relatedProduct.setName("関連商品");
        relatedProduct.setCategory("MAIN");

        when(productService.findById(1L)).thenReturn(product);
        when(productService.findRelatedProducts("MAIN", 1L)).thenReturn(List.of(relatedProduct));

        Model model = new ConcurrentModel();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = productController.detail(1L, model, response, request, null);

        assertEquals("products/detail", viewName);
        assertTrue(model.containsAttribute("product"));
        assertTrue(model.containsAttribute("relatedProducts"));
        assertEquals(false, model.getAttribute("isFavorite"));
        assertNotNull(response.getCookie("recentProducts"));

        verify(productService).findById(1L);
        verify(productService).findRelatedProducts("MAIN", 1L);
        verifyNoInteractions(favoriteService);
    }

    @Test
    void 最近見た商品Cookieがある場合は一覧画面にrecentProductsを入れる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("テスト商品");

        Page<Product> page = new PageImpl<>(List.of(product));

        when(productService.searchWithPaging(null, null, 0)).thenReturn(page);
        when(productService.findByIds(List.of(1L, 2L))).thenReturn(List.of(product));

        Model model = new ConcurrentModel();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("recentProducts", "1-2"));

        String viewName = productController.list(null, null, 0, model, request);

        assertEquals("products/list", viewName);
        assertTrue(model.containsAttribute("recentProducts"));

        verify(productService).findByIds(List.of(1L, 2L));
    }
}