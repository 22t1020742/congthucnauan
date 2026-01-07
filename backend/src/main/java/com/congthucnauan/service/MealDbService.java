package com.congthucnauan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MealDbService {
    
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1";
    
    @Autowired
    private RestTemplate restTemplate;
    
    public String searchMealsByName(String query) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/search.php")
                .queryParam("s", query)
                .toUriString();
        
        return restTemplate.getForObject(url, String.class);
    }
    
    public String getMealById(String id) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/lookup.php")
                .queryParam("i", id)
                .toUriString();
        
        return restTemplate.getForObject(url, String.class);
    }
    
    public String getRandomMeal() {
        String url = BASE_URL + "/random.php";
        return restTemplate.getForObject(url, String.class);
    }
    
    public String getCategories() {
        String url = BASE_URL + "/categories.php";
        return restTemplate.getForObject(url, String.class);
    }
    
    public String getMealsByCategory(String category) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/filter.php")
                .queryParam("c", category)
                .toUriString();
        
        return restTemplate.getForObject(url, String.class);
    }
    
    public String getMealsByArea(String area) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/filter.php")
                .queryParam("a", area)
                .toUriString();
        
        return restTemplate.getForObject(url, String.class);
    }
    
    public String getAreas() {
        String url = BASE_URL + "/list.php?a=list";
        return restTemplate.getForObject(url, String.class);
    }
    
    public String searchByFirstLetter(String letter) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/search.php")
                .queryParam("f", letter)
                .toUriString();
        
        return restTemplate.getForObject(url, String.class);
    }
}
