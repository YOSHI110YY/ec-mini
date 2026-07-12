package com.example.ecmini.controller;

import com.example.ecmini.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class FavoriteApiController {

    private final FavoriteService favoriteService;

    public FavoriteApiController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // お気に入り一覧をJSONで返す
    @GetMapping
    public List<?> listFavorites(Principal principal) {

        String username = principal.getName();

        return favoriteService.getFavoriteProducts(username);
    }

    // お気に入り追加
    @PostMapping("/{productId}")
    public ResponseEntity<Void> addFavorite(
            @PathVariable Long productId,
            Principal principal
    ) {
        String username = principal.getName();

        favoriteService.addFavorite(username, productId);

        return ResponseEntity.noContent().build();
    }

    // お気に入り削除
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long productId,
            Principal principal
    ) {
        String username = principal.getName();

        favoriteService.removeFavorite(username, productId);

        return ResponseEntity.noContent().build();
    }
}