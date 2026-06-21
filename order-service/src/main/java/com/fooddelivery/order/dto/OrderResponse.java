package com.fooddelivery.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {
    private UUID id;
    private String userId;
    private UUID restaurantId;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
}