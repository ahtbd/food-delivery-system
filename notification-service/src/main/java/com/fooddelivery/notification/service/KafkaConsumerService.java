package com.fooddelivery.notification.service;

import com.fooddelivery.notification.dto.NotificationRequest;
import com.fooddelivery.notification.dto.OrderEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    
    private final NotificationService notificationService;
    private final RabbitMQProducerService rabbitMQProducerService;
    private final ObjectMapper objectMapper;
    
    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consumeOrderEvent(String message) {
        try {
            log.info("Received Kafka message: {}", message);
            
            OrderEventDTO event = objectMapper.readValue(message, OrderEventDTO.class);
            
            // Create notification from order event
            NotificationRequest request = new NotificationRequest();
            request.setUserId(event.getUserId());
            request.setOrderId(event.getOrderId());
            request.setType("EMAIL");
            request.setSubject("Order " + event.getEventType());
            request.setMessage("Your order " + event.getOrderId() + " has been " + event.getEventType());
            request.setRecipient("user@example.com"); // In real app, get from user service
            request.setPriority("HIGH");
            
            // Save notification
            notificationService.createNotification(request)
                .flatMap(response -> {
                    // Send to RabbitMQ for processing
                    rabbitMQProducerService.sendNotification(response);
                    return Mono.empty();
                })
                .subscribe(
                    null,
                    error -> log.error("Error processing Kafka message: {}", error.getMessage())
                );
            
        } catch (Exception e) {
            log.error("Error consuming Kafka message: {}", e.getMessage());
        }
    }
}