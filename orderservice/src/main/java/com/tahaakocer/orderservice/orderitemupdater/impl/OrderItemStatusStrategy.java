package com.tahaakocer.orderservice.orderitemupdater.impl;

import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.mapper.OrderStatusMapper;
import com.tahaakocer.orderservice.model.*;
import com.tahaakocer.orderservice.orderitemupdater.OrderItemUpdateStrategy;
import com.tahaakocer.orderservice.repository.mongo.OrderItemRepository;
import com.tahaakocer.orderservice.repository.mongo.OrderItemStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Component
@Slf4j
public class OrderItemStatusStrategy implements OrderItemUpdateStrategy {
    private final OrderStatusMapper orderStatusMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemStatusRepository orderItemStatusRepository;

    public OrderItemStatusStrategy(OrderStatusMapper orderStatusMapper,
                                   OrderItemRepository orderItemRepository, OrderItemStatusRepository orderItemStatusRepository) {
        this.orderStatusMapper = orderStatusMapper;
        this.orderItemRepository = orderItemRepository;
        this.orderItemStatusRepository = orderItemStatusRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getActiveStatusDefinedBy() != null;
    }

    @Override
    public boolean objectStatus(BaseOrderItem orderItem) {
        return orderItem.getActiveStatusDefinedBy() != null;
    }

    @Override
    public void update(BaseOrderItem orderItem, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;
        try {
            OrderStatus currentStatus = orderItem.getActiveStatusDefinedBy();
            this.orderStatusMapper.updateOrderStatusFromDto(currentStatus, updateDTO.getActiveStatusDefinedBy());
            saveOrderItemStatusHistory(currentStatus,orderItem);
            setBaseModelUpdateProperties(orderItem);
            this.orderItemRepository.save(orderItem);
        } catch (Exception e) {
            log.error("Error while updating Order Item status: " + e.getMessage());
            throw new GeneralException("Error while updating Order Item status: " + e.getMessage());
        }
    }

    @Override
    public void create(BaseOrderItem orderItem, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;
        try {
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setId(UUID.randomUUID());
            this.orderStatusMapper.updateOrderStatusFromDto(orderStatus, updateDTO.getActiveStatusDefinedBy());
            orderItem.setActiveStatusDefinedBy(orderStatus);
            saveOrderItemStatusHistory(orderStatus, orderItem);
            setBaseModelCreateProperties(orderItem);
            this.orderItemRepository.save(orderItem);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GeneralException("Error while creating Order Item status: " + e.getMessage());
        }

    }
    private void saveOrderItemStatusHistory(OrderStatus status, BaseOrderItem orderItem) {
        OrderItemStatus orderItemStatus = orderItemStatusRepository.findByOrderItemRef_OrderItemId(orderItem.getId())
                .orElse(
                        OrderItemStatus.builder().id(UUID.randomUUID()).build()
                );

        orderItemStatus.setState(status.getState());
        orderItemStatus.setSubState(status.getSubState());
        orderItemStatus.setDescription(status.getDescription());
        orderItemStatus.setEventName(status.getEventName());
        orderItemStatus.setStartDate(status.getStartDate());

        StatusHistory statusHistory = StatusHistory.builder()
                .id(UUID.randomUUID())
                .state(status.getState())
                .subState(status.getSubState())
                .description(status.getDescription())
                .eventName(status.getEventName())
                .startDate(status.getStartDate() != null ? status.getStartDate() : LocalDateTime.now())
                .build();

        if (orderItemStatus.getOrderItemStatusHistory() == null) {
            orderItemStatus.setOrderItemStatusHistory(new ArrayList<>());
        }
        orderItemStatus.getOrderItemStatusHistory().add(statusHistory);

        OrderItemRef orderItemRef = OrderItemRef.builder()
                .id(UUID.randomUUID())
                .orderItemId(orderItem.getId())
                .code(orderItem.getCode())
                .build();
        orderItemStatus.setOrderItemRef(orderItemRef);
        // Kaydet
        this.orderItemStatusRepository.save(orderItemStatus);
    }
}
