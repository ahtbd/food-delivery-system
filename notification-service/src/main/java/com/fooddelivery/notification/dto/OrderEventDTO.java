package com.fooddelivery.notification.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderEventDTO {
    private String eventType;
    private String orderId;
    private String userId;
    private String restaurantId;
    private String timestamp;
}