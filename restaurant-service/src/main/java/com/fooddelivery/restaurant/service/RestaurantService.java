package com.fooddelivery.restaurant.service;

import com.fooddelivery.restaurant.dto.RestaurantRequest;
import com.fooddelivery.restaurant.dto.RestaurantResponse;
import com.fooddelivery.restaurant.mapper.RestaurantMapper;
import com.fooddelivery.restaurant.model.Restaurant;
import com.fooddelivery.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public Mono<RestaurantResponse> createRestaurant(RestaurantRequest request) {
        return Mono.just(request)
            .map(restaurantMapper::toRestaurant)
            .map(restaurant -> {
                // ✅ Force set ID to null for INSERT
                restaurant.setId(null);
                return restaurant;
            })
            .flatMap(restaurantRepository::save)
            .map(restaurantMapper::toResponse)
            .doOnSuccess(response -> log.info("Restaurant created: {}", response.getName()));
    }

    public Mono<RestaurantResponse> getRestaurantById(UUID id) {
        return restaurantRepository.findById(id)
            .map(restaurantMapper::toResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Restaurant not found with id: " + id)));
    }

    public Flux<RestaurantResponse> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findAll()
            .skip((long) page * size)
            .take(size)
            .map(restaurantMapper::toResponse);
    }

    public Flux<RestaurantResponse> getRestaurantsByCuisine(String cuisineType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findByCuisineType(cuisineType, pageable)
            .map(restaurantMapper::toResponse);
    }

    public Flux<RestaurantResponse> getActiveRestaurants() {
        return restaurantRepository.findByIsActiveTrue()
            .map(restaurantMapper::toResponse);
    }

    public Flux<RestaurantResponse> getTopRestaurants(int limit) {
        return restaurantRepository.findTopRestaurants(limit, 0)
            .map(restaurantMapper::toResponse);
    }

    public Flux<RestaurantResponse> searchRestaurants(String name) {
        return restaurantRepository.searchRestaurantsByName(name)
            .map(restaurantMapper::toResponse);
    }

    public Mono<RestaurantResponse> updateRestaurant(UUID id, RestaurantRequest request) {
        return restaurantRepository.findById(id)
            .flatMap(existingRestaurant -> {
                Restaurant updatedRestaurant = restaurantMapper.toRestaurant(request);
                updatedRestaurant.setId(id);
                updatedRestaurant.setRating(existingRestaurant.getRating());
                updatedRestaurant.setTotalReviews(existingRestaurant.getTotalReviews());
                updatedRestaurant.setCreatedAt(existingRestaurant.getCreatedAt());
                updatedRestaurant.setUpdatedAt(LocalDateTime.now());
                return restaurantRepository.save(updatedRestaurant);
            })
            .map(restaurantMapper::toResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Restaurant not found with id: " + id)));
    }

    public Mono<Void> deleteRestaurant(UUID id) {
        return restaurantRepository.findById(id)
            .flatMap(restaurant -> {
                restaurant.setIsActive(false);
                restaurant.setUpdatedAt(LocalDateTime.now());
                return restaurantRepository.save(restaurant).then();
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Restaurant not found with id: " + id)));
    }

    public Mono<RestaurantResponse> updateRating(UUID id, BigDecimal rating) {
        return restaurantRepository.findById(id)
            .flatMap(restaurant -> {
                int currentReviews = restaurant.getTotalReviews() != null ? restaurant.getTotalReviews() : 0;
                BigDecimal currentRating = restaurant.getRating() != null ? restaurant.getRating() : BigDecimal.ZERO;

                BigDecimal newRating = currentRating.multiply(BigDecimal.valueOf(currentReviews))
                    .add(rating)
                    .divide(BigDecimal.valueOf(currentReviews + 1), 2, java.math.RoundingMode.HALF_UP);

                restaurant.setRating(newRating);
                restaurant.setTotalReviews(currentReviews + 1);
                restaurant.setUpdatedAt(LocalDateTime.now());
                return restaurantRepository.save(restaurant);
            })
            .map(restaurantMapper::toResponse);
    }
}