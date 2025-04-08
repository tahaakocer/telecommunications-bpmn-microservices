package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.order.OrderRequestResponse;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {SuperBuilder.class})
public interface OrderRequestMapper {

    OrderRequestResponse entityToResponse(OrderRequest orderRequest);
}
