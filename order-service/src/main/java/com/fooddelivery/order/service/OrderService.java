package com.fooddelivery.order.service;

import com.fooddelivery.order.dto.OrderRequest;
import com.fooddelivery.order.dto.OrderResponse;
import com.fooddelivery.order.mapper.OrderMapper;
import com.fooddelivery.order.model.Order;
import com.fooddelivery.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final KafkaProducerService kafkaProducerService;

    public Mono<OrderResponse> createOrder(OrderRequest request) {
        return Mono.just(request)
            .map(orderMapper::toOrder)
            .map(order -> {
                // ✅ Force set ID to null for INSERT
                order.setId(null);
                return order;
            })
            .flatMap(orderRepository::save)
            .doOnSuccess(order -> {
                log.info("Order created: {}", order.getId());
                kafkaProducerService.sendOrderCreatedEvent(
                    order.getId(),
                    order.getUserId(),
                    order.getRestaurantId()
                );
            })
            .map(orderMapper::toResponse);
    }

    public Mono<OrderResponse> getOrderById(UUID id) {
        return orderRepository.findById(id)
            .map(orderMapper::toResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Order not found with id: " + id)));
    }

    public Flux<OrderResponse> getOrdersByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByUserId(userId, pageable)
            .map(orderMapper::toResponse);
    }

    public Flux<OrderResponse> getOrdersByRestaurant(UUID restaurantId, int limit, int offset) {
        return orderRepository.findOrdersByRestaurantWithPagination(restaurantId, limit, offset)
            .map(orderMapper::toResponse);
    }

    public Mono<OrderResponse> updateOrderStatus(UUID id, String status) {
        return orderRepository.findById(id)
            .flatMap(order -> {
                order.setStatus(status);
                order.setUpdatedAt(LocalDateTime.now());
                return orderRepository.save(order);
            })
            .map(orderMapper::toResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Order not found with id: " + id)));
    }

    public Mono<Void> cancelOrder(UUID id) {
        return orderRepository.findById(id)
            .flatMap(order -> {
                if ("DELIVERED".equals(order.getStatus())) {
                    return Mono.error(new RuntimeException("Cannot cancel delivered order"));
                }
                order.setStatus("CANCELLED");
                order.setUpdatedAt(LocalDateTime.now());
                return orderRepository.save(order).then();
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Order not found with id: " + id)));
    }

    public Mono<Long> getOrderCountByStatus(String status) {
        return orderRepository.countByStatus(status);
    }
}