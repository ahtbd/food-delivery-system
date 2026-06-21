package com.fooddelivery.order.service;

import com.fooddelivery.order.dto.OrderRequest;
import com.fooddelivery.order.dto.OrderResponse;
import com.fooddelivery.order.mapper.OrderMapper;
import com.fooddelivery.order.model.Order;
import com.fooddelivery.order.repository.OrderRepository;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest orderRequest;
    private Order order;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        orderRequest = new OrderRequest();
        orderRequest.setUserId("user123");
        orderRequest.setRestaurantId(restaurantId);
        orderRequest.setItems("{\"item\": \"Pizza\", \"quantity\": 2}");
        orderRequest.setTotalAmount(BigDecimal.valueOf(25.99));
        orderRequest.setDeliveryAddress("123 Main St, Dhaka");

        order = Order.builder()
            .id(orderId)
            .userId("user123")
            .restaurantId(restaurantId)
            .items("{\"item\": \"Pizza\", \"quantity\": 2}")
            .totalAmount(BigDecimal.valueOf(25.99))
            .status("PENDING")
            .deliveryAddress("123 Main St, Dhaka")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        orderResponse = OrderResponse.builder()
            .id(orderId)
            .userId("user123")
            .restaurantId(restaurantId)
            .totalAmount(BigDecimal.valueOf(25.99))
            .status("PENDING")
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void createOrder_ShouldReturnOrderResponse() {
        when(orderMapper.toOrder(any(OrderRequest.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);

        StepVerifier.create(orderService.createOrder(orderRequest))
            .expectNextMatches(response -> 
                response.getUserId().equals("user123") &&
                response.getStatus().equals("PENDING")
            )
            .verifyComplete();
    }

    @Test
    void getOrderById_ShouldReturnOrderResponse() {
        UUID orderId = order.getId();
        when(orderRepository.findById(orderId)).thenReturn(Mono.just(order));
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);

        StepVerifier.create(orderService.getOrderById(orderId))
            .expectNextMatches(response -> 
                response.getId().equals(orderId) &&
                response.getUserId().equals("user123")
            )
            .verifyComplete();
    }

    @Test
    void getOrderById_WhenNotFound_ShouldReturnError() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Mono.empty());

        StepVerifier.create(orderService.getOrderById(orderId))
            .expectErrorMatches(error -> 
                error instanceof RuntimeException &&
                error.getMessage().contains("Order not found")
            )
            .verify();
    }

    @Test
    void getOrdersByUser_ShouldReturnOrderResponses() {
        String userId = "user123";
        when(orderRepository.findByUserId(userId, PageRequest.of(0, 10)))
            .thenReturn(Flux.just(order));
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);

        StepVerifier.create(orderService.getOrdersByUser(userId, 0, 10))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void updateOrderStatus_ShouldReturnUpdatedOrder() {
        UUID orderId = order.getId();
        String newStatus = "CONFIRMED";
        order.setStatus(newStatus);

        when(orderRepository.findById(orderId)).thenReturn(Mono.just(order));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));
        when(orderMapper.toResponse(any(Order.class))).thenReturn(
            OrderResponse.builder()
                .id(orderId)
                .userId("user123")
                .status(newStatus)
                .build()
        );

        StepVerifier.create(orderService.updateOrderStatus(orderId, newStatus))
            .expectNextMatches(response -> 
                response.getStatus().equals(newStatus)
            )
            .verifyComplete();
    }

    @Test
    void getOrderCountByStatus_ShouldReturnCount() {
        String status = "PENDING";
        when(orderRepository.countByStatus(status)).thenReturn(Mono.just(5L));

        StepVerifier.create(orderService.getOrderCountByStatus(status))
            .expectNext(5L)
            .verifyComplete();
    }
}