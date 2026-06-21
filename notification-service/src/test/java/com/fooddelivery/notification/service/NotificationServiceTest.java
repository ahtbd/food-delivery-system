package com.fooddelivery.notification.service;

import com.fooddelivery.notification.dto.NotificationRequest;
import com.fooddelivery.notification.dto.NotificationResponse;
import com.fooddelivery.notification.model.Notification;
import com.fooddelivery.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private NotificationRequest notificationRequest;
    private Notification notification;
    private NotificationResponse notificationResponse;

    @BeforeEach
    void setUp() {
        String notificationId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();

        notificationRequest = new NotificationRequest();
        notificationRequest.setUserId("user123");
        notificationRequest.setOrderId(orderId);
        notificationRequest.setType("EMAIL");
        notificationRequest.setSubject("Order Confirmation");
        notificationRequest.setMessage("Your order has been confirmed!");
        notificationRequest.setRecipient("user@example.com");
        notificationRequest.setPriority("HIGH");

        notification = Notification.builder()
            .id(notificationId)
            .userId("user123")
            .orderId(orderId)
            .type("EMAIL")
            .channel("REST_API")
            .subject("Order Confirmation")
            .message("Your order has been confirmed!")
            .recipient("user@example.com")
            .status("PENDING")
            .priority("HIGH")
            .retryCount(0)
            .maxRetries(3)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        notificationResponse = NotificationResponse.builder()
            .id(notificationId)
            .userId("user123")
            .orderId(orderId)
            .type("EMAIL")
            .channel("REST_API")
            .subject("Order Confirmation")
            .message("Your order has been confirmed!")
            .recipient("user@example.com")
            .status("PENDING")
            .priority("HIGH")
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void createNotification_ShouldReturnNotificationResponse() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(Mono.just(notification));

        StepVerifier.create(notificationService.createNotification(notificationRequest))
            .expectNextMatches(response -> 
                response.getUserId().equals("user123") &&
                response.getType().equals("EMAIL") &&
                response.getStatus().equals("PENDING")
            )
            .verifyComplete();
    }

    @Test
    void getNotificationById_ShouldReturnNotificationResponse() {
        String notificationId = notification.getId();
        when(notificationRepository.findById(notificationId)).thenReturn(Mono.just(notification));

        StepVerifier.create(notificationService.getNotificationById(notificationId))
            .expectNextMatches(response -> 
                response.getId().equals(notificationId) &&
                response.getUserId().equals("user123")
            )
            .verifyComplete();
    }

    @Test
    void getNotificationById_WhenNotFound_ShouldReturnError() {
        String notificationId = UUID.randomUUID().toString();
        when(notificationRepository.findById(notificationId)).thenReturn(Mono.empty());

        StepVerifier.create(notificationService.getNotificationById(notificationId))
            .expectErrorMatches(error -> 
                error instanceof RuntimeException &&
                error.getMessage().contains("Notification not found")
            )
            .verify();
    }

    @Test
    void getNotificationsByUser_ShouldReturnNotificationResponses() {
        String userId = "user123";
        when(notificationRepository.findByUserId(userId, PageRequest.of(0, 10)))
            .thenReturn(Flux.just(notification));

        StepVerifier.create(notificationService.getNotificationsByUser(userId, 0, 10))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getNotificationsByOrder_ShouldReturnNotificationResponses() {
        String orderId = notification.getOrderId();
        when(notificationRepository.findByOrderId(orderId))
            .thenReturn(Flux.just(notification));

        StepVerifier.create(notificationService.getNotificationsByOrder(orderId))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getPendingNotifications_ShouldReturnNotificationResponses() {
        when(notificationRepository.findPendingNotifications())
            .thenReturn(Flux.just(notification));

        StepVerifier.create(notificationService.getPendingNotifications())
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void updateNotificationStatus_ShouldReturnUpdatedNotification() {
        String notificationId = notification.getId();
        String newStatus = "SENT";
        notification.setStatus(newStatus);

        when(notificationRepository.findById(notificationId)).thenReturn(Mono.just(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(Mono.just(notification));

        StepVerifier.create(notificationService.updateNotificationStatus(notificationId, newStatus))
            .expectNextMatches(response -> 
                response.getStatus().equals(newStatus)
            )
            .verifyComplete();
    }

    @Test
    void markAsSent_ShouldComplete() {
        String notificationId = notification.getId();
        when(notificationRepository.findById(notificationId)).thenReturn(Mono.just(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(Mono.just(notification));

        // Use verifyComplete() for Mono<Void>
        StepVerifier.create(notificationService.markAsSent(notificationId))
            .expectComplete()
            .verify();
    }

    @Test
    void markAsFailed_ShouldComplete() {
        String notificationId = notification.getId();
        String errorMessage = "Failed to send email";
        when(notificationRepository.findById(notificationId)).thenReturn(Mono.just(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(Mono.just(notification));

        // Use verifyComplete() for Mono<Void>
        StepVerifier.create(notificationService.markAsFailed(notificationId, errorMessage))
            .expectComplete()
            .verify();
    }
}