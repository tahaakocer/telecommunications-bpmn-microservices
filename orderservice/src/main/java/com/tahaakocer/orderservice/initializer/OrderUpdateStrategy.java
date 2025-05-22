package com.tahaakocer.orderservice.initializer;

import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.utils.KeycloakUtil;

import java.time.LocalDateTime;

public interface OrderUpdateStrategy {

    boolean canHandle(OrderUpdateDto updateDTO);

    boolean objectStatus (OrderRequest order);
    void update(OrderRequest order, OrderUpdateDto updateDTO);

    void create(OrderRequest order, OrderUpdateDto updateDTO);

    default void setOrderRequestUpdateProperties(OrderRequest order) {
        order.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
        order.setUpdateDate(LocalDateTime.now());
    }

    default void setOrderRequestCreateProperties(OrderRequest order) {
        order.setCreateDate(LocalDateTime.now());
        order.setCreatedBy(KeycloakUtil.getKeycloakUsername());
        order.setLastModifiedBy(order.getCreatedBy());
        order.setUpdateDate(order.getCreateDate());
    }
    default void setBaseOrderUpdateProperties(OrderRequest order) {
        order.getBaseOrder().setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
        order.getBaseOrder().setUpdateDate(LocalDateTime.now());
    }
    default void setBaseOrderCreateProperties(OrderRequest order) {
        order.getBaseOrder().setCreateDate(LocalDateTime.now());
        order.getBaseOrder().setCreatedBy(KeycloakUtil.getKeycloakUsername());
        order.getBaseOrder().setLastModifiedBy(order.getBaseOrder().getCreatedBy());
        order.getBaseOrder().setUpdateDate(order.getBaseOrder().getCreateDate());
    }
}
