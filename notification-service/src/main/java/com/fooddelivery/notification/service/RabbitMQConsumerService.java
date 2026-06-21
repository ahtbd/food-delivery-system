package com.fooddelivery.notification.service;

import com.fooddelivery.notification.dto.NotificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumerService {
    
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    
    @RabbitListener(queues = "notification.queue")
    public void consumeNotification(String message) {
        try {
            log.info("Received RabbitMQ message: {}", message);
            
            NotificationResponse notification = objectMapper.readValue(message, NotificationResponse.class);
            
            // Process notification (send email, SMS, etc.)
            log.info("Processing notification for user: {}, type: {}", 
                notification.getUserId(), notification.getType());
            
            // Simulate sending
            Thread.sleep(1000);
            
            // Mark as sent
            notificationService.markAsSent(notification.getId())
                .subscribe(
                    null,
                    error -> log.error("Error marking notification as sent: {}", error.getMessage())
                );
            
            log.info("Notification processed successfully: {}", notification.getId());
            
        } catch (Exception e) {
            log.error("Error consuming RabbitMQ message: {}", e.getMessage());
        }
    }
}