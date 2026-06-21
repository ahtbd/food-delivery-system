package com.fooddelivery.restaurant.service;

import com.fooddelivery.restaurant.dto.MenuItemRequest;
import com.fooddelivery.restaurant.dto.MenuItemResponse;
import com.fooddelivery.restaurant.mapper.MenuItemMapper;
import com.fooddelivery.restaurant.model.MenuItem;
import com.fooddelivery.restaurant.repository.MenuItemRepository;
import com.fooddelivery.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuItemService {
    
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;
    
    public Mono<MenuItemResponse> addMenuItem(MenuItemRequest request) {
        return restaurantRepository.existsById(request.getRestaurantId())
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new RuntimeException("Restaurant not found with id: " + request.getRestaurantId()));
                }
                return Mono.just(request)
                    .map(menuItemMapper::toMenuItem)
                    .map(menuItem -> {
                        // ✅ Force set ID to null for INSERT
                        menuItem.setId(null);
                        return menuItem;
                    })
                    .flatMap(menuItemRepository::save)
                    .map(menuItemMapper::toResponse);
            })
            .doOnSuccess(response -> log.info("Menu item added: {}", response.getName()));
    }
    
    public Flux<MenuItemResponse> getMenuItemsByRestaurant(UUID restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId)
            .map(menuItemMapper::toResponse);
    }
    
    public Flux<MenuItemResponse> getAvailableMenuItems(UUID restaurantId) {
        return menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId)
            .map(menuItemMapper::toResponse);
    }
    
    public Flux<MenuItemResponse> getMenuItemsByCategory(UUID restaurantId, String category) {
        return menuItemRepository.findByRestaurantIdAndCategory(restaurantId, category)
            .map(menuItemMapper::toResponse);
    }
    
    public Flux<MenuItemResponse> getItemsByPriceRange(UUID restaurantId, BigDecimal minPrice, BigDecimal maxPrice) {
        return menuItemRepository.findItemsByPriceRange(restaurantId, minPrice, maxPrice)
            .map(menuItemMapper::toResponse);
    }
    
    public Flux<MenuItemResponse> getVegetarianItems(UUID restaurantId) {
        return menuItemRepository.findVegetarianItems(restaurantId)
            .map(menuItemMapper::toResponse);
    }
    
    public Mono<MenuItemResponse> updateMenuItem(UUID id, MenuItemRequest request) {
        return menuItemRepository.findById(id)
            .flatMap(existingItem -> {
                MenuItem updatedItem = menuItemMapper.toMenuItem(request);
                updatedItem.setId(id);
                updatedItem.setCreatedAt(existingItem.getCreatedAt());
                updatedItem.setUpdatedAt(LocalDateTime.now());
                return menuItemRepository.save(updatedItem);
            })
            .map(menuItemMapper::toResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Menu item not found with id: " + id)));
    }
    
    public Mono<Void> deleteMenuItem(UUID id) {
        return menuItemRepository.findById(id)
            .flatMap(menuItem -> {
                menuItem.setIsAvailable(false);
                menuItem.setUpdatedAt(LocalDateTime.now());
                return menuItemRepository.save(menuItem).then();
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Menu item not found with id: " + id)));
    }
    
    public Mono<Void> toggleAvailability(UUID id) {
        return menuItemRepository.findById(id)
            .flatMap(menuItem -> {
                menuItem.setIsAvailable(!menuItem.getIsAvailable());
                menuItem.setUpdatedAt(LocalDateTime.now());
                return menuItemRepository.save(menuItem).then();
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Menu item not found with id: " + id)));
    }
}