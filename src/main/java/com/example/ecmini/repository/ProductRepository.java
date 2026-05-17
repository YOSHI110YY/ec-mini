package com.example.ecmini.repository;

import com.example.ecmini.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//ProductエンティティをDBとつなぐ窓口
public interface ProductRepository extends JpaRepository<Product, Long> {

    //商品名の部分一致検索(自動でSQLが生成される）
    List<Product> findByNameContaining(String keyword);
    //カテゴリ検索(これもSQL自動生成)
    List<Product> findByCategory(String category);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
    Page<Product> searchByName(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.id <> :id")
    List<Product> findRelatedProducts(@Param("category") String category, @Param("id") Long id);

    @Query("SELECT p FROM Product p " +
            "WHERE (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:category IS NULL OR p.category = :category)")
    List<Product> searchProducts(@Param("name") String name,
                                 @Param("category") String category);

    @Query("""
    SELECT p FROM Product p
    WHERE (:name IS NULL OR p.name LIKE %:name%)
      AND (:category IS NULL OR :category = '' OR p.category = :category)
""")
    Page<Product> search(@Param("name") String name,
                         @Param("category") String category,
                         Pageable pageable);

    List<Product> findTop4ByCategoryAndIdNot(String category, Long id);

    long countByStockLessThanEqual(int stock);

}