package com.example.ecmini.repository;

import com.example.ecmini.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository
        extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUsername(String username);

    boolean existsByUsernameAndProductId(
            String username,
            Long productId
    );

    void deleteByUsernameAndProductId(
            String username,
            Long productId
    );

}