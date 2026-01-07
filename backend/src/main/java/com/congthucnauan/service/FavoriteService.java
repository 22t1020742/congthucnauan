package com.congthucnauan.service;

import com.congthucnauan.dto.FavoriteRequest;
import com.congthucnauan.dto.FavoriteResponse;
import com.congthucnauan.entity.Favorite;
import com.congthucnauan.entity.User;
import com.congthucnauan.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private AuthService authService;
    
    @Transactional
    public FavoriteResponse addFavorite(FavoriteRequest request) {
        User currentUser = authService.getCurrentUser();
        
        if (favoriteRepository.existsByUserIdAndRecipeId(currentUser.getId(), request.getRecipeId())) {
            throw new RuntimeException("Recipe already in favorites");
        }
        
        Favorite favorite = new Favorite();
        favorite.setUser(currentUser);
        favorite.setRecipeId(request.getRecipeId());
        
        Favorite savedFavorite = favoriteRepository.save(favorite);
        
        return mapToFavoriteResponse(savedFavorite);
    }
    
    public List<FavoriteResponse> getFavorites() {
        User currentUser = authService.getCurrentUser();
        List<Favorite> favorites = favoriteRepository.findByUserId(currentUser.getId());
        return favorites.stream()
                .map(this::mapToFavoriteResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void removeFavorite(String recipeId) {
        User currentUser = authService.getCurrentUser();
        
        if (!favoriteRepository.existsByUserIdAndRecipeId(currentUser.getId(), recipeId)) {
            throw new RuntimeException("Favorite not found");
        }
        
        favoriteRepository.deleteByUserIdAndRecipeId(currentUser.getId(), recipeId);
    }
    
    public boolean isFavorite(String recipeId) {
        User currentUser = authService.getCurrentUser();
        return favoriteRepository.existsByUserIdAndRecipeId(currentUser.getId(), recipeId);
    }
    
    private FavoriteResponse mapToFavoriteResponse(Favorite favorite) {
        FavoriteResponse response = new FavoriteResponse();
        response.setId(favorite.getId());
        response.setRecipeId(favorite.getRecipeId());
        response.setCreatedAt(favorite.getCreatedAt());
        
        return response;
    }
}
