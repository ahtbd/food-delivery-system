package com.fooddelivery.notification.repository;

import com.fooddelivery.notification.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {
    
    // Derived Query - Find by userId
    Flux<Notification> findByUserId(String userId);
    
    // Derived Query - Find by userId with pagination
    Flux<Notification> findByUserId(String userId, Pageable pageable);
    
    // Derived Query - Find by orderId
    Flux<Notification> findByOrderId(String orderId);
    
    // Derived Query - Find by status
    Flux<Notification> findByStatus(String status);
    
    // Custom Query - Find pending notifications
    @Query("{ 'status': 'PENDING' }")
    Flux<Notification> findPendingNotifications();
    
    // Custom Query - Find by userId and status
    @Query("{ 'userId': ?0, 'status': ?1 }")
    Flux<Notification> findByUserIdAndStatus(String userId, String status);
    
    // Custom Query - Find failed notifications with retry count less than max
    @Query("{ 'status': 'FAILED', $expr: { $lt: ['$retryCount', '$maxRetries'] } }")
    Flux<Notification> findRetryableNotifications();
    
    // Count by status
    Mono<Long> countByStatus(String status);
    
    // Count by userId
    Mono<Long> countByUserId(String userId);
}