package com.fooddelivery.notification.service;

import com.fooddelivery.notification.dto.NotificationRequest;
import com.fooddelivery.notification.dto.NotificationResponse;
import com.fooddelivery.notification.model.Notification;
import com.fooddelivery.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    public Mono<NotificationResponse> createNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
            .userId(request.getUserId())
            .orderId(request.getOrderId())
            .type(request.getType())
            .channel("REST_API")
            .subject(request.getSubject())
            .message(request.getMessage())
            .recipient(request.getRecipient())
            .status("PENDING")
            .priority(request.getPriority() != null ? request.getPriority() : "MEDIUM")
            .retryCount(0)
            .maxRetries(3)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        return notificationRepository.save(notification)
            .map(this::toResponse)
            .doOnSuccess(response -> log.info("Notification created: {}", response.getId()));
    }
    
    public Mono<NotificationResponse> getNotificationById(String id) {
        return notificationRepository.findById(id)
            .map(this::toResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Notification not found with id: " + id)));
    }
    
    public Flux<NotificationResponse> getNotificationsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByUserId(userId, pageable)
            .map(this::toResponse);
    }
    
    public Flux<NotificationResponse> getNotificationsByOrder(String orderId) {
        return notificationRepository.findByOrderId(orderId)
            .map(this::toResponse);
    }
    
    public Flux<NotificationResponse> getPendingNotifications() {
        return notificationRepository.findPendingNotifications()
            .map(this::toResponse);
    }
    
    public Mono<NotificationResponse> updateNotificationStatus(String id, String status) {
        return notificationRepository.findById(id)
            .flatMap(notification -> {
                notification.setStatus(status);
                if ("SENT".equals(status)) {
                    notification.setSentAt(LocalDateTime.now());
                }
                notification.setUpdatedAt(LocalDateTime.now());
                return notificationRepository.save(notification);
            })
            .map(this::toResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Notification not found with id: " + id)));
    }
    
    public Mono<Void> markAsSent(String id) {
        return notificationRepository.findById(id)
            .flatMap(notification -> {
                notification.setStatus("SENT");
                notification.setSentAt(LocalDateTime.now());
                notification.setUpdatedAt(LocalDateTime.now());
                return notificationRepository.save(notification);
            })
            .then()  // ✅ Convert to Mono<Void>
            .onErrorResume(throwable -> Mono.error(new RuntimeException("Notification not found with id: " + id)));
    }
    
    public Mono<Void> markAsFailed(String id, String errorMessage) {
        return notificationRepository.findById(id)
            .flatMap(notification -> {
                notification.setStatus("FAILED");
                notification.setErrorMessage(errorMessage);
                notification.setRetryCount(notification.getRetryCount() + 1);
                notification.setUpdatedAt(LocalDateTime.now());
                return notificationRepository.save(notification);
            })
            .then()  // ✅ Convert to Mono<Void>
            .onErrorResume(throwable -> Mono.error(new RuntimeException("Notification not found with id: " + id)));
    }
    
    private NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
            .id(notification.getId())
            .userId(notification.getUserId())
            .orderId(notification.getOrderId())
            .type(notification.getType())
            .channel(notification.getChannel())
            .subject(notification.getSubject())
            .message(notification.getMessage())
            .recipient(notification.getRecipient())
            .status(notification.getStatus())
            .priority(notification.getPriority())
            .sentAt(notification.getSentAt())
            .createdAt(notification.getCreatedAt())
            .build();
    }
}