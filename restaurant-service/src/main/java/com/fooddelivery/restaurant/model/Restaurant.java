package com.fooddelivery.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("restaurants")
public class Restaurant {
    @Id
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
    private LocalDateTime updatedAt;
}