package com.tahaakocer.orderservice.initializer;

import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;

public interface OrderUpdateStrategy {

    boolean canHandle(OrderUpdateDto updateDTO);

    boolean objectStatus (OrderRequest order);
    void update(OrderRequest order, OrderUpdateDto updateDTO);

    void create(OrderRequest order, OrderUpdateDto updateDTO);

}
