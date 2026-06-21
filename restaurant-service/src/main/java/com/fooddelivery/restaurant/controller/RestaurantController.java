package com.fooddelivery.restaurant.controller;

import com.fooddelivery.restaurant.dto.RestaurantRequest;
import com.fooddelivery.restaurant.dto.RestaurantResponse;
import com.fooddelivery.restaurant.service.RestaurantService;
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
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant Management", description = "APIs for managing restaurants")
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new restaurant")
    public Mono<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantRequest request) {
        return restaurantService.createRestaurant(request);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID")
    public Mono<RestaurantResponse> getRestaurantById(@PathVariable UUID id) {
        return restaurantService.getRestaurantById(id);
    }
    
    @GetMapping
    @Operation(summary = "Get all restaurants with pagination")
    public Flux<RestaurantResponse> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.getAllRestaurants(page, size);
    }
    
    @GetMapping("/cuisine/{cuisineType}")
    @Operation(summary = "Get restaurants by cuisine type")
    public Flux<RestaurantResponse> getRestaurantsByCuisine(
            @PathVariable String cuisineType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.getRestaurantsByCuisine(cuisineType, page, size);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get all active restaurants")
    public Flux<RestaurantResponse> getActiveRestaurants() {
        return restaurantService.getActiveRestaurants();
    }
    
    @GetMapping("/top")
    @Operation(summary = "Get top rated restaurants")
    public Flux<RestaurantResponse> getTopRestaurants(
            @RequestParam(defaultValue = "10") int limit) {
        return restaurantService.getTopRestaurants(limit);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search restaurants by name")
    public Flux<RestaurantResponse> searchRestaurants(
            @RequestParam String name) {
        return restaurantService.searchRestaurants(name);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update restaurant")
    public Mono<RestaurantResponse> updateRestaurant(
            @PathVariable UUID id,
            @Valid @RequestBody RestaurantRequest request) {
        return restaurantService.updateRestaurant(id, request);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete restaurant (soft delete)")
    public Mono<Void> deleteRestaurant(@PathVariable UUID id) {
        return restaurantService.deleteRestaurant(id);
    }
    
    @PutMapping("/{id}/rating")
    @Operation(summary = "Update restaurant rating")
    public Mono<RestaurantResponse> updateRating(
            @PathVariable UUID id,
            @RequestParam BigDecimal rating) {
        return restaurantService.updateRating(id, rating);
    }
}