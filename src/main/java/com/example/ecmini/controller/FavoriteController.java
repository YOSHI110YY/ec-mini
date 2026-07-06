package com.example.ecmini.controller;

import org.springframework.ui.Model;
import com.example.ecmini.service.FavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites/add/{productId}")
    public String addFavorite(@PathVariable Long productId, Principal principal) {

        String username = principal.getName();

        favoriteService.addFavorite(username, productId);

        return "redirect:/products/" + productId;
    }

    @PostMapping("/favorites/remove/{productId}")
    public String removeFavorite(@PathVariable Long productId, Principal principal) {

        String username = principal.getName();

        favoriteService.removeFavorite(username, productId);

        return "redirect:/products/" + productId;
    }
    @GetMapping("/favorites")
    public String listFavorites(Model model, Principal principal) {

        String username = principal.getName();

        model.addAttribute("favoriteProducts",
                favoriteService.getFavoriteProducts(username));

        return "favorites/list";
    }

}