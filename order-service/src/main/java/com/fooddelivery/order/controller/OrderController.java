package com.fooddelivery.order.controller;

import com.fooddelivery.order.dto.OrderRequest;
import com.fooddelivery.order.dto.OrderResponse;
import com.fooddelivery.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing food delivery orders")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new order")
    public Mono<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public Mono<OrderResponse> getOrderById(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders by user ID with pagination")
    public Flux<OrderResponse> getOrdersByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrdersByUser(userId, page, size);
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get orders by restaurant with pagination")
    public Flux<OrderResponse> getOrdersByRestaurant(
            @PathVariable UUID restaurantId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return orderService.getOrdersByRestaurant(restaurantId, limit, offset);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public Mono<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel an order")
    public Mono<Void> cancelOrder(@PathVariable UUID id) {
        return orderService.cancelOrder(id);
    }
    
    @GetMapping("/count/{status}")
    @Operation(summary = "Get order count by status")
    public Mono<Long> getOrderCountByStatus(@PathVariable String status) {
        return orderService.getOrderCountByStatus(status);
    }
}