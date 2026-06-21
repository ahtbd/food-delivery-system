package com.fooddelivery.notification.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotBlank(message = "User ID is required")
    private String userId;
    
    private String orderId;
    
    @NotBlank(message = "Type is required")
    private String type;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Message is required")
    private String message;
    
    @NotBlank(message = "Recipient is required")
    private String recipient;
    
    private String priority = "MEDIUM";
}