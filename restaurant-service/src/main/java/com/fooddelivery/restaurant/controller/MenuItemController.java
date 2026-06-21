package com.fooddelivery.restaurant.controller;

import com.fooddelivery.restaurant.dto.MenuItemRequest;
import com.fooddelivery.restaurant.dto.MenuItemResponse;
import com.fooddelivery.restaurant.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Item Management", description = "APIs for managing restaurant menu items")
public class MenuItemController {
    
    private final MenuItemService menuItemService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new menu item")
    public Mono<MenuItemResponse> addMenuItem(@Valid @RequestBody MenuItemRequest request) {
        return menuItemService.addMenuItem(request);
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get all menu items by restaurant")
    public Flux<MenuItemResponse> getMenuItemsByRestaurant(@PathVariable UUID restaurantId) {
        return menuItemService.getMenuItemsByRestaurant(restaurantId);
    }
    
    @GetMapping("/restaurant/{restaurantId}/available")
    @Operation(summary = "Get available menu items by restaurant")
    public Flux<MenuItemResponse> getAvailableMenuItems(@PathVariable UUID restaurantId) {
        return menuItemService.getAvailableMenuItems(restaurantId);
    }
    
    @GetMapping("/restaurant/{restaurantId}/category/{category}")
    @Operation(summary = "Get menu items by category")
    public Flux<MenuItemResponse> getMenuItemsByCategory(
            @PathVariable UUID restaurantId,
            @PathVariable String category) {
        return menuItemService.getMenuItemsByCategory(restaurantId, category);
    }
    
    @GetMapping("/restaurant/{restaurantId}/price-range")
    @Operation(summary = "Get menu items by price range")
    public Flux<MenuItemResponse> getItemsByPriceRange(
            @PathVariable UUID restaurantId,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return menuItemService.getItemsByPriceRange(restaurantId, minPrice, maxPrice);
    }
    
    @GetMapping("/restaurant/{restaurantId}/vegetarian")
    @Operation(summary = "Get vegetarian menu items")
    public Flux<MenuItemResponse> getVegetarianItems(@PathVariable UUID restaurantId) {
        return menuItemService.getVegetarianItems(restaurantId);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update menu item")
    public Mono<MenuItemResponse> updateMenuItem(
            @PathVariable UUID id,
            @Valid @RequestBody MenuItemRequest request) {
        return menuItemService.updateMenuItem(id, request);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete menu item (soft delete)")
    public Mono<Void> deleteMenuItem(@PathVariable UUID id) {
        return menuItemService.deleteMenuItem(id);
    }
    
    @PatchMapping("/{id}/toggle-availability")
    @Operation(summary = "Toggle menu item availability")
    public Mono<Void> toggleAvailability(@PathVariable UUID id) {
        return menuItemService.toggleAvailability(id);
    }
}