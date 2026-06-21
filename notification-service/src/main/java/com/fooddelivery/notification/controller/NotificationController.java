package com.fooddelivery.notification.controller;

import com.fooddelivery.notification.dto.NotificationRequest;
import com.fooddelivery.notification.dto.NotificationResponse;
import com.fooddelivery.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "APIs for managing notifications")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new notification")
    public Mono<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {
        return notificationService.createNotification(request);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID")
    public Mono<NotificationResponse> getNotificationById(@PathVariable String id) {
        return notificationService.getNotificationById(id);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications by user ID with pagination")
    public Flux<NotificationResponse> getNotificationsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return notificationService.getNotificationsByUser(userId, page, size);
    }
    
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get notifications by order ID")
    public Flux<NotificationResponse> getNotificationsByOrder(@PathVariable String orderId) {
        return notificationService.getNotificationsByOrder(orderId);
    }
    
    @GetMapping("/pending")
    @Operation(summary = "Get all pending notifications")
    public Flux<NotificationResponse> getPendingNotifications() {
        return notificationService.getPendingNotifications();
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update notification status")
    public Mono<NotificationResponse> updateNotificationStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return notificationService.updateNotificationStatus(id, status);
    }
}