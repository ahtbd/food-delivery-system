package com.fooddelivery.notification.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private String id;
    private String userId;
    private String orderId;
    private String type;
    private String channel;
    private String subject;
    private String message;
    private String recipient;
    private String status;
    private String priority;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}