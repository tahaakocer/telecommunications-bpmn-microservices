package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.commondto.order.OrderStatusDto;
import com.tahaakocer.orderservice.model.mongo.OrderStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OrderStatusMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrderStatusFromDto(@MappingTarget OrderStatus target, OrderStatusDto source);
}
