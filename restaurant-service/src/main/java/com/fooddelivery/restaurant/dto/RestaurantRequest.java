package com.fooddelivery.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestaurantRequest {
    @NotBlank(message = "Restaurant name is required")
    private String name;
    
    private String description;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    private String email;
    
    @NotBlank(message = "Cuisine type is required")
    private String cuisineType;
    
    private String operatingHours;
    
    private BigDecimal deliveryFee;
    
    private BigDecimal minimumOrderAmount;
    
    private Boolean isActive = true;
    
    private Boolean isAcceptingOrders = true;
}