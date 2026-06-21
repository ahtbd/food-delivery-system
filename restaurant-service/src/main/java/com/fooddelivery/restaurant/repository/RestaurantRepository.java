package com.fooddelivery.restaurant.repository;

import com.fooddelivery.restaurant.model.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

public interface RestaurantRepository extends R2dbcRepository<Restaurant, UUID> {
    
    Flux<Restaurant> findByCuisineType(String cuisineType);
    
    Flux<Restaurant> findByCuisineType(String cuisineType, Pageable pageable);
    
    Flux<Restaurant> findByIsActiveTrue();
    
    @Query("SELECT * FROM restaurants WHERE rating >= $1 ORDER BY rating DESC")
    Flux<Restaurant> findRestaurantsByRatingGreaterThan(BigDecimal minRating);
    
    @Query("SELECT * FROM restaurants WHERE LOWER(name) LIKE LOWER(CONCAT('%', $1, '%'))")
    Flux<Restaurant> searchRestaurantsByName(String name);
    
    @Query("SELECT * FROM restaurants WHERE is_active = true ORDER BY rating DESC LIMIT $1 OFFSET $2")
    Flux<Restaurant> findTopRestaurants(int limit, int offset);
    
    Mono<Long> countByCuisineType(String cuisineType);
}