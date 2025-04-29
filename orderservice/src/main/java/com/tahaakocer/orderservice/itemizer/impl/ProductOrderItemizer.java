package com.tahaakocer.orderservice.itemizer.impl;

import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.itemizer.OrderItemizable;
import com.tahaakocer.orderservice.itemizer.OrderItemizer;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.model.mongo.OrderRequestRef;
import com.tahaakocer.orderservice.model.mongo.ProductOrder;
import com.tahaakocer.orderservice.model.mongo.ProductOrderItem;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@OrderItemizable(orderType = ProductOrder.class)
public class ProductOrderItemizer implements OrderItemizer<ProductOrderItem> {

    @Override
    public List<ProductOrderItem> itemize(OrderRequest order) {
        if (order == null) {
            throw new GeneralException("Order cannot be null");
        }

        List<ProductOrderItem> items = new ArrayList<>();
        //TODO
        ProductOrder productOrder = (ProductOrder) order.getBaseOrder();
        if (productOrder.getProducts() == null || productOrder.getProducts().isEmpty()) {
            throw new GeneralException("Product list cannot be null or empty");
        }
        productOrder.getProducts().forEach(product -> {
            ProductOrderItem item = new ProductOrderItem();
            item.setId(UUID.randomUUID());
//            item.setBpmnFlowRef(order.getBaseOrder().getBpmnFlowRef());
            item.setOrderRequestRef(createOrderRequestRef(order));
            item.setProduct(product);
            item.setOrderType(order.getBaseOrder().getOrderType());
            item.setCreateDate(LocalDateTime.now());
            item.setCreatedBy(KeycloakUtil.getKeycloakUsername());
            item.setLastModifiedBy(item.getCreatedBy());
            item.setUpdateDate(item.getCreateDate());
            items.add(item);
        });
        log.info("itemizer - Order request: " + order);
        return items;
    }
    private OrderRequestRef createOrderRequestRef(OrderRequest order) {
        return OrderRequestRef.builder()
                .id(UUID.randomUUID())
                .orderRequestId(order.getId())
                .code(order.getCode())
                .orderDate(order.getCreateDate())
                .orderType(order.getBaseOrder().getOrderType())
                .bpmnFlowRef(order.getBaseOrder().getBpmnFlowRef())
                .channel(order.getChannel())
                .isDraft(order.getBaseOrder().getIsDraft())
                .build();
    }
}