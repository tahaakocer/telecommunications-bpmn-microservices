package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.commondto.order.OrderItemDto;
import com.tahaakocer.orderservice.model.BaseOrderItem;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface OrderItemMapper {

    OrderItemDto entityToDto(BaseOrderItem orderItem);
}
