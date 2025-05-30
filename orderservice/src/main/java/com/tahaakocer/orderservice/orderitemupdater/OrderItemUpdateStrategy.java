package com.tahaakocer.orderservice.orderitemupdater;

import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.model.BaseOrderItem;
import com.tahaakocer.orderservice.utils.KeycloakUtil;

import java.time.LocalDateTime;

public interface OrderItemUpdateStrategy {
    boolean canHandle(OrderUpdateDto updateDTO);
    boolean objectStatus (BaseOrderItem orderItem);
    void update(BaseOrderItem orderItem, OrderUpdateDto updateDTO);

    void create(BaseOrderItem orderItem, OrderUpdateDto updateDTO);

    default void setBaseModelUpdateProperties(BaseOrderItem orderItem) {
        orderItem.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
        orderItem.setUpdateDate(LocalDateTime.now());
    }
    default void setBaseModelCreateProperties(BaseOrderItem orderItem) {
        orderItem.setCreateDate(LocalDateTime.now());
        orderItem.setCreatedBy(KeycloakUtil.getKeycloakUsername());
        orderItem.setLastModifiedBy(orderItem.getCreatedBy());
        orderItem.setUpdateDate(orderItem.getCreateDate());
    }
}
