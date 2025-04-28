package com.tahaakocer.orderservice.mapper;


import com.tahaakocer.commondto.order.BaseOrderDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.ProductOrderDto;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import com.tahaakocer.orderservice.model.mongo.BaseOrder;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.model.mongo.ProductOrder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderRequestMapper {

    OrderRequestDto entityToDto(OrderRequest orderRequest);

    OrderRequestResponse entityToResponse(OrderRequest orderRequest);

    @SubclassMapping(source = ProductOrder.class, target = ProductOrderDto.class)
    BaseOrderDto baseOrderToBaseOrderDto(BaseOrder baseOrder);

    ProductOrderDto productOrderToProductOrderDto(ProductOrder productOrder);
}