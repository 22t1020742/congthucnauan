package com.congthucnauan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
    private String id;
    private String title;
    private String imageUrl;
    private Integer readyInMinutes;
    private Integer servings;
    private String sourceUrl;
    private String summary;
    private String instructions;
}
