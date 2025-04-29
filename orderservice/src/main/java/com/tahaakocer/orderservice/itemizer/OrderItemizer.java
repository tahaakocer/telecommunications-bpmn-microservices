package com.tahaakocer.orderservice.itemizer;

import com.tahaakocer.orderservice.model.mongo.BaseOrderItem;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;

import java.util.List;

public interface OrderItemizer<T extends BaseOrderItem> {
    List<T> itemize(OrderRequest order);
}