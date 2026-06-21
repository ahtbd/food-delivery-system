package com.fooddelivery.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private String userId;
    private String orderId;
    private String type;          // "EMAIL", "SMS", "PUSH_NOTIFICATION"
    private String channel;       // "KAFKA", "RABBITMQ"
    private String subject;
    private String message;
    private String recipient;     // email or phone number
    private String status;        // "PENDING", "SENT", "FAILED", "RETRY"
    private String priority;      // "HIGH", "MEDIUM", "LOW"
    private Integer retryCount;
    private Integer maxRetries;
    private String errorMessage;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}