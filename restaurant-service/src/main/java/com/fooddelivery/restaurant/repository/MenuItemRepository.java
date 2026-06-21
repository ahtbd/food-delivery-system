package com.fooddelivery.restaurant.repository;

import com.fooddelivery.restaurant.model.MenuItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

public interface MenuItemRepository extends R2dbcRepository<MenuItem, UUID> {
    
    // Derived Query - Find by restaurant
    Flux<MenuItem> findByRestaurantId(UUID restaurantId);
    
    // Derived Query - Find available items by restaurant
    Flux<MenuItem> findByRestaurantIdAndIsAvailableTrue(UUID restaurantId);
    
    // Derived Query - Find by restaurant and category
    Flux<MenuItem> findByRestaurantIdAndCategory(UUID restaurantId, String category);
    
    // Custom Query - Find items by price range
    @Query("SELECT * FROM menu_items WHERE restaurant_id = $1 AND price BETWEEN $2 AND $3")
    Flux<MenuItem> findItemsByPriceRange(UUID restaurantId, BigDecimal minPrice, BigDecimal maxPrice);
    
    // Custom Query - Find vegetarian items
    @Query("SELECT * FROM menu_items WHERE restaurant_id = $1 AND is_vegetarian = true")
    Flux<MenuItem> findVegetarianItems(UUID restaurantId);
    
    // Custom Query - Count available items
    @Query("SELECT COUNT(*) FROM menu_items WHERE restaurant_id = $1 AND is_available = true")
    Mono<Long> countAvailableItemsByRestaurant(UUID restaurantId);
}