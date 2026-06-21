package com.fooddelivery.order.repository;

import com.fooddelivery.order.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderRepository extends R2dbcRepository<Order, UUID> {
    
    // Derived Query
    Flux<Order> findByUserId(String userId);
    
    // Derived Query with Pagination
    Flux<Order> findByUserId(String userId, Pageable pageable);
    
    // Custom Query
    @Query("SELECT * FROM orders WHERE user_id = $1 AND status = $2")
    Flux<Order> findOrdersByUserAndStatus(String userId, String status);
    
    // Custom Query with Pagination
    @Query("SELECT * FROM orders WHERE restaurant_id = $1 ORDER BY created_at DESC LIMIT $2 OFFSET $3")
    Flux<Order> findOrdersByRestaurantWithPagination(UUID restaurantId, int limit, int offset);
    
    // Count by status
    Mono<Long> countByStatus(String status);
}