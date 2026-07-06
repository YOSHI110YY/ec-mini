package com.example.ecmini.service;

import com.example.ecmini.entity.Favorite;
import com.example.ecmini.entity.Product;

import java.util.List;

public interface FavoriteService {

    void addFavorite(String username, Long productId);

    void removeFavorite(String username, Long productId);

    List<Favorite> getFavorites(String username);

    List<Product> getFavoriteProducts(String username);

    boolean isFavorite(String username, Long productId);

}


