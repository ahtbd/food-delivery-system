package com.fooddelivery.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MenuItemRequest {
    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;
    
    @NotBlank(message = "Item name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    private BigDecimal price;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private Boolean isAvailable = true;
    
    private Boolean isVegetarian = false;
    
    private Boolean isVegan = false;
    
    private Boolean isGlutenFree = false;
    
    private Integer preparationTime = 15;
    
    private String imageUrl;
}