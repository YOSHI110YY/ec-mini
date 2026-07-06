package com.example.ecmini.service;

import com.example.ecmini.entity.Product;
import com.example.ecmini.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void findById_商品が存在する場合_商品を取得できる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Chicken", result.getName());
    }

    @Test
    void findById_商品が存在しない場合_例外が発生する() {
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.findById(99L));

        assertEquals("商品が見つかりません: 99", exception.getMessage());
    }

    @Test
    void findAll_商品一覧を取得できる() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Chicken");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Salad");

        //Mock Repositoryの返却設定
        when(productRepository.findAll())
                .thenReturn(List.of(product1, product2));

        //Service実行
        List<Product> result = productService.findAll();

        //結果確認
        assertEquals(2, result.size());
        assertEquals("Chicken", result.get(0).getName());
        assertEquals("Salad", result.get(1).getName());
    }

    @Test
    void findByCategory_カテゴリで商品を取得できる() {
        Product product = new Product();
        product.setId(1L);
        product.setCategory("MAIN");

        when(productRepository.findByCategory("MAIN"))
                .thenReturn(List.of(product));

        List<Product> result = productService.findByCategory("MAIN");

        assertEquals(1, result.size());
        assertEquals("MAIN", result.get(0).getCategory());
    }

    @Test
    void searchByName_商品名で検索できる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken Bowl");

        when(productRepository.findByNameContaining("Chicken"))
                .thenReturn(List.of(product));

        List<Product> result = productService.searchByName("Chicken");

        assertEquals(1, result.size());
        assertEquals("Chicken Bowl", result.get(0).getName());
    }

    @Test
    void deleteById_商品を削除できる() {
        productService.deleteById(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void save_商品を保存できる() {
        Product product = new Product();
        product.setName("Chicken");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Chicken");

        when(productRepository.save(product))
                .thenReturn(savedProduct);

        Product result = productService.save(product);

        assertEquals(1L, result.getId());
        assertEquals("Chicken", result.getName());
    }

    @Test
    void getImagePath_画像がある場合_画像パスを返す() {
        Product product = new Product();
        product.setImage("chicken.jpg");

        String result = productService.getImagePath(product);

        assertEquals("/uploads/product/chicken.jpg", result);
    }

    @Test
    void getImagePath_画像がnullの場合_noimageを返す() {
        Product product = new Product();
        product.setImage(null);

        String result = productService.getImagePath(product);

        assertEquals("/images/noimage.png", result);
    }

    @Test
    void getImagePath_画像が空文字の場合_noimageを返す() {
        Product product = new Product();
        product.setImage("");

        String result = productService.getImagePath(product);

        assertEquals("/images/noimage.png", result);
    }

    @Test
    void findRelatedProducts_関連商品を取得できる() {
        Product product = new Product();
        product.setId(2L);
        product.setCategory("MAIN");

        when(productRepository.findRelatedProducts("MAIN", 1L))
                .thenReturn(List.of(product));

        List<Product> result = productService.findRelatedProducts("MAIN", 1L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
    }

    @Test
    void findByIds_IDリストで商品を取得できる() {
        Product product1 = new Product();
        product1.setId(1L);

        Product product2 = new Product();
        product2.setId(2L);

        when(productRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(product1, product2));

        List<Product> result = productService.findByIds(List.of(1L, 2L));

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void searchProducts_条件で商品を検索できる() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chicken");
        product.setCategory("MAIN");

        when(productRepository.searchProducts("Chicken", "MAIN"))
                .thenReturn(List.of(product));

        List<Product> result = productService.searchProducts("Chicken", "MAIN");

        assertEquals(1, result.size());
        assertEquals("Chicken", result.get(0).getName());
        assertEquals("MAIN", result.get(0).getCategory());
    }
    @Test
    void update_画像なしで商品情報を更新できる() {
        Product existing = new Product();
        existing.setId(1L);
        existing.setName("Old");
        existing.setPrice(500);
        existing.setStock(3);
        existing.setCategory("OLD");
        existing.setImage("old.jpg");

        Product updateProduct = new Product();
        updateProduct.setName("New");
        updateProduct.setPrice(800);
        updateProduct.setStock(10);
        updateProduct.setCategory("MAIN");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        productService.update(1L, updateProduct, null);

        assertEquals("New", existing.getName());
        assertEquals(800, existing.getPrice());
        assertEquals(10, existing.getStock());
        assertEquals("MAIN", existing.getCategory());
        assertEquals("old.jpg",existing.getImage());

        verify(productRepository, times(1)).save(existing);
    }

    @Test
    void searchWithPaging_検索条件なしの場合_findAllが呼ばれる() {
        Page<Product> page = Page.empty();

        when(productRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.searchWithPaging(null, null, 0);

        assertEquals(page, result);
        verify(productRepository, times(1)).findAll(any(Pageable.class));
        verify(productRepository, never()).search(any(), any(), any(Pageable.class));
    }

    @Test
    void searchWithPaging_検索条件ありの場合_searchが呼ばれる() {
        Page<Product> page = Page.empty();

        when(productRepository.search(eq("Chicken"), eq("MAIN"), any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.searchWithPaging("Chicken", "MAIN", 0);

        assertEquals(page, result);
        verify(productRepository, times(1)).search(eq("Chicken"), eq("MAIN"), any(Pageable.class));
        verify(productRepository, never()).findAll(any(Pageable.class));
    }
    @Test
    void create_画像なしで商品を登録できる() {
        Product product = new Product();
        product.setName("Chicken");
        product.setPrice(800);
        product.setStock(10);
        product.setCategory("MAIN");

        productService.create(product, null);

        assertNull(product.getImage());

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void searchWithPaging_nameだけありの場合_searchが呼ばれる() {
        Page<Product> page = Page.empty();

        when(productRepository.search(eq("Chicken"), isNull(), any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.searchWithPaging("Chicken", null, 0);

        assertEquals(page, result);

        verify(productRepository, times(1))
                .search(eq("Chicken"), isNull(), any(Pageable.class));

        verify(productRepository, never())
                .findAll(any(Pageable.class));
    }

    @Test
    void searchWithPaging_categoryだけありの場合_searchが呼ばれる() {
        Page<Product> page = Page.empty();

        when(productRepository.search(isNull(), eq("MAIN"), any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.searchWithPaging(null, "MAIN", 0);

        assertEquals(page, result);

        verify(productRepository, times(1))
                .search(isNull(), eq("MAIN"), any(Pageable.class));

        verify(productRepository, never())
                .findAll(any(Pageable.class));
    }

}