package com.tahaakocer.orderservice.itemizer;

import com.tahaakocer.orderservice.model.BaseOrderItem;
import com.tahaakocer.orderservice.model.OrderRequest;

import java.util.List;

public interface OrderItemizer<T extends BaseOrderItem> {
    List<T> itemize(OrderRequest order);
}