package com.example.ecmini.service;

import com.example.ecmini.entity.Favorite;
import com.example.ecmini.entity.Product;
import com.example.ecmini.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductService productService;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               ProductService productService) {
        this.favoriteRepository = favoriteRepository;
        this.productService = productService;
    }

    @Override
    public void addFavorite(String username, Long productId) {
        if (!favoriteRepository.existsByUsernameAndProductId(username, productId)) {
            Favorite favorite = new Favorite();
            favorite.setUsername(username);
            favorite.setProductId(productId);
            favorite.setCreatedAt(LocalDateTime.now());

            favoriteRepository.save(favorite);
        }
    }
    @Transactional
    @Override
    public void removeFavorite(String username, Long productId) {
        favoriteRepository.deleteByUsernameAndProductId(username, productId);
    }

    @Override
    public List<Favorite> getFavorites(String username) {
        return favoriteRepository.findByUsername(username);
    }
    @Override
    public List<Product> getFavoriteProducts(String username) {
        List<Favorite> favorites = favoriteRepository.findByUsername(username);

        List<Long> productIds = favorites.stream()
                .map(Favorite::getProductId)
                .toList();

        return productService.findByIds(productIds);
    }


    @Override
    public boolean isFavorite(String username, Long productId) {
        return favoriteRepository.existsByUsernameAndProductId(username, productId);
    }
}