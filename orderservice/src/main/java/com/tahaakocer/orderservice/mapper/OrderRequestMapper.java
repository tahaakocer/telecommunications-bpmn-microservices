package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.request.OrderRequestResponse;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper {

    OrderRequestResponse entityToResponse(OrderRequest orderRequest);
}
