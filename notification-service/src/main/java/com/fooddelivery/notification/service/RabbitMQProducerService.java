package com.fooddelivery.notification.service;

import com.fooddelivery.notification.dto.NotificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducerService {
    
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    
    private static final String EXCHANGE_NAME = "notification.exchange";
    private static final String ROUTING_KEY = "notification.routing.key";
    
    public void sendNotification(NotificationResponse notification) {
        try {
            String message = objectMapper.writeValueAsString(notification);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
            log.info("Notification sent to RabbitMQ: {}", notification.getId());
        } catch (Exception e) {
            log.error("Error sending to RabbitMQ: {}", e.getMessage());
        }
    }
}