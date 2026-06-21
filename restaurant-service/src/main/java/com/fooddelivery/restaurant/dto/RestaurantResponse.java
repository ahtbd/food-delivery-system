package com.fooddelivery.restaurant.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RestaurantResponse {
    private UUID id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String cuisineType;
    private BigDecimal rating;
    private Integer totalReviews;
    private String operatingHours;
    private BigDecimal deliveryFee;
    private BigDecimal minimumOrderAmount;
    private Boolean isActive;
    private Boolean isAcceptingOrders;
    private LocalDateTime createdAt;
}