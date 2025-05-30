package com.tahaakocer.orderservice.initializer;

import com.tahaakocer.orderservice.dto.initializer.InitializerDto;
import com.tahaakocer.orderservice.model.BaseOrder;

public interface OrderFactory<T extends BaseOrder> {

    String getOrderType();

    T createOrder(InitializerDto initializerDto, String orderType);
}
