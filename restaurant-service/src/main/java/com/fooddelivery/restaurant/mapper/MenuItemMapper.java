package com.fooddelivery.restaurant.mapper;

import com.fooddelivery.restaurant.dto.MenuItemRequest;
import com.fooddelivery.restaurant.dto.MenuItemResponse;
import com.fooddelivery.restaurant.model.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface MenuItemMapper {
    
    MenuItemMapper INSTANCE = Mappers.getMapper(MenuItemMapper.class);
    
    @Mapping(target = "id", ignore = true)              // ✅ Ignore ID - auto-generated
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    MenuItem toMenuItem(MenuItemRequest request);
    
    MenuItemResponse toResponse(MenuItem menuItem);
}