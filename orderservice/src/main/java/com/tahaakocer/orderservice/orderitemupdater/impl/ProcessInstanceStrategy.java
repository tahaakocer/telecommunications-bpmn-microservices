package com.tahaakocer.orderservice.orderitemupdater.impl;

import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.model.BaseOrderItem;
import com.tahaakocer.orderservice.orderitemupdater.OrderItemUpdateStrategy;
import com.tahaakocer.orderservice.repository.mongo.OrderItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcessInstanceStrategy implements OrderItemUpdateStrategy {
    private final OrderItemRepository orderItemRepository;

    public ProcessInstanceStrategy(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getProcessInstanceId() != null;
    }

    @Override
    public boolean objectStatus(BaseOrderItem orderItem) {
        return orderItem.getProcessInstanceId() != null;
    }

    @Override
    public void update(BaseOrderItem orderItem, OrderUpdateDto updateDTO) {
        if(!canHandle(updateDTO)) return;
        try{
            orderItem.setProcessInstanceId(updateDTO.getProcessInstanceId());
            setBaseModelUpdateProperties(orderItem);
            this.orderItemRepository.save(orderItem);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new GeneralException("Error while updating ProcessInstance in Order Item: " + e.getMessage());
        }
    }

    @Override
    public void create(BaseOrderItem orderItem, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;

        try{
            orderItem.setProcessInstanceId(updateDTO.getProcessInstanceId());
            setBaseModelCreateProperties(orderItem);
            this.orderItemRepository.save(orderItem);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new GeneralException("Error while creating ProcessInstance in Order Item: " + e.getMessage());
        }


    }
}
