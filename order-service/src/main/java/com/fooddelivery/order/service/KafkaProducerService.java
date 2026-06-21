package com.fooddelivery.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    public void sendOrderCreatedEvent(UUID orderId, String userId, UUID restaurantId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "ORDER_CREATED");
            event.put("orderId", orderId.toString());
            event.put("userId", userId);
            event.put("restaurantId", restaurantId.toString());
            event.put("timestamp", java.time.LocalDateTime.now().toString());
            
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order-events", orderId.toString(), message);
            log.info("Kafka event sent for order: {}", orderId);
        } catch (Exception e) {
            log.error("Failed to send Kafka event: {}", e.getMessage());
        }
    }
}