package com.fooddelivery.restaurant.service;

import com.fooddelivery.restaurant.dto.RestaurantRequest;
import com.fooddelivery.restaurant.dto.RestaurantResponse;
import com.fooddelivery.restaurant.mapper.RestaurantMapper;
import com.fooddelivery.restaurant.model.Restaurant;
import com.fooddelivery.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    private RestaurantRequest restaurantRequest;
    private Restaurant restaurant;
    private RestaurantResponse restaurantResponse;

    @BeforeEach
    void setUp() {
        UUID restaurantId = UUID.randomUUID();

        restaurantRequest = new RestaurantRequest();
        restaurantRequest.setName("Pizza Palace");
        restaurantRequest.setDescription("Best Pizza in Town");
        restaurantRequest.setAddress("123 Food St, Dhaka");
        restaurantRequest.setPhone("+880123456789");
        restaurantRequest.setEmail("info@pizzapalace.com");
        restaurantRequest.setCuisineType("Italian");
        restaurantRequest.setOperatingHours("10AM-10PM");
        restaurantRequest.setDeliveryFee(BigDecimal.valueOf(2.99));
        restaurantRequest.setMinimumOrderAmount(BigDecimal.valueOf(10.00));
        restaurantRequest.setIsActive(true);
        restaurantRequest.setIsAcceptingOrders(true);

        restaurant = Restaurant.builder()
            .id(restaurantId)
            .name("Pizza Palace")
            .description("Best Pizza in Town")
            .address("123 Food St, Dhaka")
            .phone("+880123456789")
            .email("info@pizzapalace.com")
            .cuisineType("Italian")
            .rating(BigDecimal.ZERO)
            .totalReviews(0)
            .operatingHours("10AM-10PM")
            .deliveryFee(BigDecimal.valueOf(2.99))
            .minimumOrderAmount(BigDecimal.valueOf(10.00))
            .isActive(true)
            .isAcceptingOrders(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        restaurantResponse = RestaurantResponse.builder()
            .id(restaurantId)
            .name("Pizza Palace")
            .description("Best Pizza in Town")
            .address("123 Food St, Dhaka")
            .phone("+880123456789")
            .email("info@pizzapalace.com")
            .cuisineType("Italian")
            .rating(BigDecimal.ZERO)
            .totalReviews(0)
            .operatingHours("10AM-10PM")
            .deliveryFee(BigDecimal.valueOf(2.99))
            .minimumOrderAmount(BigDecimal.valueOf(10.00))
            .isActive(true)
            .isAcceptingOrders(true)
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void createRestaurant_ShouldReturnRestaurantResponse() {
        when(restaurantMapper.toRestaurant(any(RestaurantRequest.class))).thenReturn(restaurant);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(Mono.just(restaurant));
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        StepVerifier.create(restaurantService.createRestaurant(restaurantRequest))
            .expectNextMatches(response -> 
                response.getName().equals("Pizza Palace") &&
                response.getCuisineType().equals("Italian")
            )
            .verifyComplete();
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurantResponse() {
        UUID restaurantId = restaurant.getId();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Mono.just(restaurant));
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        StepVerifier.create(restaurantService.getRestaurantById(restaurantId))
            .expectNextMatches(response -> 
                response.getId().equals(restaurantId) &&
                response.getName().equals("Pizza Palace")
            )
            .verifyComplete();
    }

    @Test
    void getRestaurantById_WhenNotFound_ShouldReturnError() {
        UUID restaurantId = UUID.randomUUID();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Mono.empty());

        StepVerifier.create(restaurantService.getRestaurantById(restaurantId))
            .expectErrorMatches(error -> 
                error instanceof RuntimeException &&
                error.getMessage().contains("Restaurant not found")
            )
            .verify();
    }

    @Test
    void getAllRestaurants_ShouldReturnRestaurantResponses() {
        when(restaurantRepository.findAll())
            .thenReturn(Flux.just(restaurant));
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        StepVerifier.create(restaurantService.getAllRestaurants(0, 10))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getRestaurantsByCuisine_ShouldReturnRestaurantResponses() {
        String cuisineType = "Italian";
        when(restaurantRepository.findByCuisineType(cuisineType, PageRequest.of(0, 10)))
            .thenReturn(Flux.just(restaurant));
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        StepVerifier.create(restaurantService.getRestaurantsByCuisine(cuisineType, 0, 10))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getActiveRestaurants_ShouldReturnRestaurantResponses() {
        when(restaurantRepository.findByIsActiveTrue())
            .thenReturn(Flux.just(restaurant));
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        StepVerifier.create(restaurantService.getActiveRestaurants())
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void searchRestaurants_ShouldReturnRestaurantResponses() {
        String name = "Pizza";
        when(restaurantRepository.searchRestaurantsByName(name))
            .thenReturn(Flux.just(restaurant));
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        StepVerifier.create(restaurantService.searchRestaurants(name))
            .expectNextCount(1)
            .verifyComplete();
    }
}