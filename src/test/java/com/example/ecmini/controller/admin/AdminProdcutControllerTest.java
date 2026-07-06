package com.example.ecmini.controller.admin;

import com.example.ecmini.entity.Product;
import com.example.ecmini.service.CategoryService;
import com.example.ecmini.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private AdminProductController adminProductController;

    @Test
    void 管理者商品一覧画面を表示できる() {
        Product product = new Product();
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productService.searchWithPaging(null, null, 0)).thenReturn(page);

        Model model = new ConcurrentModel();

        String viewName = adminProductController.list(null, null, 0, model);

        assertEquals("admin/products/list", viewName);
        assertSame(page, model.getAttribute("productPage"));
        assertEquals(page.getContent(), model.getAttribute("products"));
        assertEquals(0, model.getAttribute("currentPage"));

        verify(productService).searchWithPaging(null, null, 0);
    }

    @Test
    void 商品登録フォームを表示できる() {
        when(categoryService.findAll()).thenReturn(List.of());

        Model model = new ConcurrentModel();

        String viewName = adminProductController.showCreateForm(model);

        assertEquals("admin/products/new", viewName);
        assertTrue(model.containsAttribute("product"));
        assertEquals(List.of(), model.getAttribute("categories"));

        verify(categoryService).findAll();
    }
}