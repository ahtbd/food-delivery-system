package com.fooddelivery.order.mapper;

import com.fooddelivery.order.dto.OrderRequest;
import com.fooddelivery.order.dto.OrderResponse;
import com.fooddelivery.order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface OrderMapper {
    
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    
    @Mapping(target = "id", ignore = true)              // ✅ ID will be auto-generated
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    Order toOrder(OrderRequest request);
    
    OrderResponse toResponse(Order order);
}