package com.fooddelivery.restaurant.service;

import com.fooddelivery.restaurant.dto.MenuItemRequest;
import com.fooddelivery.restaurant.dto.MenuItemResponse;
import com.fooddelivery.restaurant.mapper.MenuItemMapper;
import com.fooddelivery.restaurant.model.MenuItem;
import com.fooddelivery.restaurant.repository.MenuItemRepository;
import com.fooddelivery.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private MenuItemService menuItemService;

    private MenuItemRequest menuItemRequest;
    private MenuItem menuItem;
    private MenuItemResponse menuItemResponse;
    private UUID restaurantId;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        menuItemRequest = new MenuItemRequest();
        menuItemRequest.setRestaurantId(restaurantId);
        menuItemRequest.setName("Margherita Pizza");
        menuItemRequest.setDescription("Classic pizza with tomato and mozzarella");
        menuItemRequest.setPrice(BigDecimal.valueOf(12.99));
        menuItemRequest.setCategory("Main Course");
        menuItemRequest.setIsAvailable(true);
        menuItemRequest.setIsVegetarian(true);
        menuItemRequest.setIsVegan(false);
        menuItemRequest.setIsGlutenFree(false);
        menuItemRequest.setPreparationTime(15);

        menuItem = MenuItem.builder()
            .id(menuItemId)
            .restaurantId(restaurantId)
            .name("Margherita Pizza")
            .description("Classic pizza with tomato and mozzarella")
            .price(BigDecimal.valueOf(12.99))
            .category("Main Course")
            .isAvailable(true)
            .isVegetarian(true)
            .isVegan(false)
            .isGlutenFree(false)
            .preparationTime(15)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        menuItemResponse = MenuItemResponse.builder()
            .id(menuItemId)
            .restaurantId(restaurantId)
            .name("Margherita Pizza")
            .description("Classic pizza with tomato and mozzarella")
            .price(BigDecimal.valueOf(12.99))
            .category("Main Course")
            .isAvailable(true)
            .isVegetarian(true)
            .isVegan(false)
            .isGlutenFree(false)
            .preparationTime(15)
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void addMenuItem_WhenRestaurantExists_ShouldReturnMenuItemResponse() {
        when(restaurantRepository.existsById(restaurantId)).thenReturn(Mono.just(true));
        when(menuItemMapper.toMenuItem(any(MenuItemRequest.class))).thenReturn(menuItem);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(Mono.just(menuItem));
        when(menuItemMapper.toResponse(any(MenuItem.class))).thenReturn(menuItemResponse);

        StepVerifier.create(menuItemService.addMenuItem(menuItemRequest))
            .expectNextMatches(response -> 
                response.getName().equals("Margherita Pizza") &&
                response.getPrice().equals(BigDecimal.valueOf(12.99))
            )
            .verifyComplete();
    }

    @Test
    void addMenuItem_WhenRestaurantNotExists_ShouldReturnError() {
        when(restaurantRepository.existsById(restaurantId)).thenReturn(Mono.just(false));

        StepVerifier.create(menuItemService.addMenuItem(menuItemRequest))
            .expectErrorMatches(error -> 
                error instanceof RuntimeException &&
                error.getMessage().contains("Restaurant not found")
            )
            .verify();
    }

    @Test
    void getMenuItemsByRestaurant_ShouldReturnMenuItemResponses() {
        when(menuItemRepository.findByRestaurantId(restaurantId))
            .thenReturn(Flux.just(menuItem));
        when(menuItemMapper.toResponse(any(MenuItem.class))).thenReturn(menuItemResponse);

        StepVerifier.create(menuItemService.getMenuItemsByRestaurant(restaurantId))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getAvailableMenuItems_ShouldReturnMenuItemResponses() {
        when(menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId))
            .thenReturn(Flux.just(menuItem));
        when(menuItemMapper.toResponse(any(MenuItem.class))).thenReturn(menuItemResponse);

        StepVerifier.create(menuItemService.getAvailableMenuItems(restaurantId))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getMenuItemsByCategory_ShouldReturnMenuItemResponses() {
        String category = "Main Course";
        when(menuItemRepository.findByRestaurantIdAndCategory(restaurantId, category))
            .thenReturn(Flux.just(menuItem));
        when(menuItemMapper.toResponse(any(MenuItem.class))).thenReturn(menuItemResponse);

        StepVerifier.create(menuItemService.getMenuItemsByCategory(restaurantId, category))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getVegetarianItems_ShouldReturnMenuItemResponses() {
        when(menuItemRepository.findVegetarianItems(restaurantId))
            .thenReturn(Flux.just(menuItem));
        when(menuItemMapper.toResponse(any(MenuItem.class))).thenReturn(menuItemResponse);

        StepVerifier.create(menuItemService.getVegetarianItems(restaurantId))
            .expectNextCount(1)
            .verifyComplete();
    }
}