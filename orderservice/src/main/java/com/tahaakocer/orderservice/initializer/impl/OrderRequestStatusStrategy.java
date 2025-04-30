package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.mapper.OrderStatusMapper;
import com.tahaakocer.orderservice.model.mongo.*;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestStatusRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Component
public class OrderRequestStatusStrategy implements OrderUpdateStrategy {
    private final OrderRequestStatusRepository orderRequestStatusRepository;
    private final OrderStatusMapper orderStatusMapper;
    private final OrderRequestRepository orderRequestRepository;

    public OrderRequestStatusStrategy(
            OrderRequestStatusRepository orderRequestStatusRepository,
            OrderStatusMapper orderStatusMapper,
            OrderRequestRepository orderRequestRepository) {
        this.orderRequestStatusRepository = orderRequestStatusRepository;
        this.orderStatusMapper = orderStatusMapper;
        this.orderRequestRepository = orderRequestRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getActiveStatusDefinedBy() != null;
    }

    @Override
    public boolean objectStatus(OrderRequest order) {
        return order.getActiveStatusDefinedBy() != null;
    }

    @Override
    public void update(OrderRequest order, OrderUpdateDto updateDTO) {
        if(!canHandle(updateDTO)) return;

        try {
            OrderStatus currentStatus = order.getActiveStatusDefinedBy();
            this.orderStatusMapper.updateOrderStatusFromDto(currentStatus, updateDTO.getActiveStatusDefinedBy());
            saveOrderRequestStatusHistory(currentStatus,order);
            order.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            order.setUpdateDate(LocalDateTime.now());
            this.orderRequestRepository.save(order);
        }
        catch (Exception e) {
            log.error("Error while updating OrderRequestStatus: " + e.getMessage());
            throw new GeneralException("Error while updating OrderRequestStatus: " + e.getMessage());
        }
    }

    @Override
    public void create(OrderRequest order, OrderUpdateDto updateDTO) {
        if(!canHandle(updateDTO)) return;

        try {
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setId(UUID.randomUUID());
            this.orderStatusMapper.updateOrderStatusFromDto(orderStatus, updateDTO.getActiveStatusDefinedBy());

            order.setActiveStatusDefinedBy(orderStatus);
            saveOrderRequestStatusHistory(orderStatus,order);

            order.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            order.setUpdateDate(LocalDateTime.now());
            this.orderRequestRepository.save(order);
        } catch (Exception e) {
            log.error("Error while creating OrderRequestStatus: " + e.getMessage());
            throw new GeneralException("Error while creating OrderRequestStatus: " + e.getMessage());
        }
    }

    private void saveOrderRequestStatusHistory(OrderStatus status, OrderRequest order) {
        OrderRequestStatus orderRequestStatus = orderRequestStatusRepository.findByOrderRequestRef_OrderRequestId(order.getId())
                .orElse(
                        OrderRequestStatus.builder().id(UUID.randomUUID()).build()
                );

        orderRequestStatus.setState(status.getState());
        orderRequestStatus.setSubState(status.getSubState());
        orderRequestStatus.setDescription(status.getDescription());
        orderRequestStatus.setEventName(status.getEventName());
        orderRequestStatus.setStartDate(status.getStartDate());

        StatusHistory statusHistory = StatusHistory.builder()
                .id(UUID.randomUUID())
                .state(status.getState())
                .subState(status.getSubState())
                .description(status.getDescription())
                .eventName(status.getEventName())
                .startDate(status.getStartDate() != null ? status.getStartDate() : LocalDateTime.now())
                .build();

        if (orderRequestStatus.getOrderRequestStatusHistory() == null) {
            orderRequestStatus.setOrderRequestStatusHistory(new ArrayList<>());
        }
        orderRequestStatus.getOrderRequestStatusHistory().add(statusHistory);

        OrderRequestRef orderRequestRef = OrderRequestRef.builder()
                .id(UUID.randomUUID())
                .orderRequestId(order.getId())
                .code(order.getCode())
                .orderDate(order.getCreateDate())
                .orderType(order.getBaseOrder().getOrderType())
                .bpmnFlowRef(order.getBaseOrder().getBpmnFlowRef())
                .channel(order.getChannel())
                .isDraft(order.getBaseOrder().getIsDraft())
                .build();
        orderRequestStatus.setOrderRequestRef(orderRequestRef);
        // Kaydet
        orderRequestStatusRepository.save(orderRequestStatus);
    }
}