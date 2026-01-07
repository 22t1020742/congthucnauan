package com.congthucnauan.controller;

import com.congthucnauan.dto.FavoriteRequest;
import com.congthucnauan.dto.FavoriteResponse;
import com.congthucnauan.dto.MessageResponse;
import com.congthucnauan.service.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @PostMapping
    public ResponseEntity<?> addFavorite(@Valid @RequestBody FavoriteRequest request) {
        try {
            FavoriteResponse response = favoriteService.addFavorite(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites() {
        List<FavoriteResponse> favorites = favoriteService.getFavorites();
        return ResponseEntity.ok(favorites);
    }
    
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> removeFavorite(@PathVariable String recipeId) {
        try {
            favoriteService.removeFavorite(recipeId);
            return ResponseEntity.ok(new MessageResponse("Favorite removed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
    
    @GetMapping("/check/{recipeId}")
    public ResponseEntity<?> checkFavorite(@PathVariable String recipeId) {
        boolean isFavorite = favoriteService.isFavorite(recipeId);
        return ResponseEntity.ok(new MessageResponse(isFavorite ? "true" : "false"));
    }
}
