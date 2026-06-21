package com.fooddelivery.order.service;

import com.fooddelivery.order.dto.OrderRequest;
import com.fooddelivery.order.dto.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResilientOrderService {
    
    private final OrderService orderService;
    
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackCreateOrder")
    public Mono<OrderResponse> createOrderWithResilience(OrderRequest request) {
        return orderService.createOrder(request);
    }
    
    public Mono<OrderResponse> fallbackCreateOrder(OrderRequest request, Throwable throwable) {
        log.warn("Circuit breaker opened for order creation: {}", throwable.getMessage());
        
        // Return a fallback response
        return Mono.just(OrderResponse.builder()
            .userId(request.getUserId())
            .status("FALLBACK")
            .build());
    }
}