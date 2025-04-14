package com.tahaakocer.orderservice.mapper;


import com.tahaakocer.orderservice.dto.order.BaseOrderDto;
import com.tahaakocer.orderservice.dto.order.OrderRequestDto;
import com.tahaakocer.orderservice.dto.order.ProductOrderDto;
import com.tahaakocer.orderservice.dto.response.OrderRequestResponse;
import com.tahaakocer.orderservice.model.mongo.BaseOrder;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.model.mongo.ProductOrder;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderRequestMapper {

    OrderRequestDto entityToDto(OrderRequest orderRequest);

    OrderRequestResponse entityToResponse(OrderRequest orderRequest);

    @SubclassMapping(source = ProductOrder.class, target = ProductOrderDto.class)
    BaseOrderDto baseOrderToBaseOrderDto(BaseOrder baseOrder);

    ProductOrderDto productOrderToProductOrderDto(ProductOrder productOrder);
}