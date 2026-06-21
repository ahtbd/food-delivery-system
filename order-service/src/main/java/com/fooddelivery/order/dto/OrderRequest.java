package com.fooddelivery.order.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderRequest {
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;
    
    @NotBlank(message = "Items are required")
    private String items;
    
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;
    
    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;
}