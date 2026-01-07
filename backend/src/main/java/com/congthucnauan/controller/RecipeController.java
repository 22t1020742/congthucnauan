package com.congthucnauan.controller;

import com.congthucnauan.service.MealDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {
    
    @Autowired
    private MealDbService mealDbService;
    
    @GetMapping("/search")
    public ResponseEntity<String> searchRecipes(@RequestParam(required = false) String query) {
        try {
            String result = mealDbService.searchMealsByName(query != null ? query : "");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<String> getRecipeInformation(@PathVariable String id) {
        try {
            String result = mealDbService.getMealById(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/random")
    public ResponseEntity<String> getRandomRecipes() {
        try {
            String result = mealDbService.getRandomMeal();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/categories")
    public ResponseEntity<String> getCategories() {
        try {
            String result = mealDbService.getCategories();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/category")
    public ResponseEntity<String> getMealsByCategory(@RequestParam String c) {
        try {
            String result = mealDbService.getMealsByCategory(c);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/area")
    public ResponseEntity<String> getMealsByArea(@RequestParam String a) {
        try {
            String result = mealDbService.getMealsByArea(a);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/areas")
    public ResponseEntity<String> getAreas() {
        try {
            String result = mealDbService.getAreas();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
