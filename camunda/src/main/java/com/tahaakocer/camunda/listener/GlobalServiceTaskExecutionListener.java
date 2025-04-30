package com.tahaakocer.camunda.listener;

import com.tahaakocer.camunda.client.OrderRequestServiceClient;
import com.tahaakocer.camunda.dto.GeneralResponse;

import com.tahaakocer.camunda.exception.GeneralException;
import com.tahaakocer.commondto.order.OrderItemDto;
import com.tahaakocer.commondto.order.OrderStatusDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class GlobalServiceTaskExecutionListener implements ExecutionListener {
    private final OrderRequestServiceClient orderRequestServiceClient;

    public GlobalServiceTaskExecutionListener(OrderRequestServiceClient orderRequestServiceClient) {
        this.orderRequestServiceClient = orderRequestServiceClient;
    }


    @Override
    public void notify(DelegateExecution execution) {
        String executionId = execution.getId();
        String processInstanceId = execution.getProcessInstanceId();
        String activityId = execution.getCurrentActivityId();
        String activityName = execution.getCurrentActivityName();
        String eventName = execution.getEventName();

        log.info("GlobalServiceTaskExecutionListener - executionId: {}" +
                        "\nprocessInstanceId: {}" +
                        "\nactivityId: {}" +
                        "\nactivityName: {}" +
                        "\neventName: {}",
                executionId, processInstanceId, activityId, activityName, eventName);

        String orderRequestId = (String) execution.getVariable("orderRequestId");
        if (orderRequestId == null) {
            log.error("GlobalServiceTaskExecutionListener - orderRequestId variable is null");
            throw new GeneralException("GlobalServiceTaskExecutionListener - orderRequestId variable is null");
        }

        String orderItemId = (String) execution.getVariable("orderItemId");
        if (orderItemId == null) {
            log.error("GlobalServiceTaskExecutionListener - orderItemId variable is null");
            throw new GeneralException("GlobalServiceTaskExecutionListener - orderItemId variable is null");
        }

        Object isMainProcessObject = execution.getVariable("isMainProcess");
        if(isMainProcessObject == null) {
            log.error("GlobalServiceTaskExecutionListener - isMainProcess variable is null");
            throw new GeneralException("GlobalServiceTaskExecutionListener - isMainProcess variable is null");
        }


        Boolean isMainProcess = (Boolean) isMainProcessObject;
        OrderUpdateDto orderUpdateDto = getOrderUpdateDto(activityId, activityName, eventName);
        if(isMainProcess) {
            callUpdateOrderRequestMethod(orderRequestId, orderUpdateDto);
        } else {
            callUpdateOrderItemMethod(orderItemId, orderUpdateDto);
        }

    }

    private void callUpdateOrderRequestMethod(String orderRequestId, OrderUpdateDto orderUpdateDto) {
        try {
            ResponseEntity<GeneralResponse<OrderRequestResponse>> response =
                    this.orderRequestServiceClient.updateOrderRequest(UUID.fromString(orderRequestId), orderUpdateDto);

        } catch (Exception e) {
            log.error("Error while updating ActiveStatusDefinedBy:" + e.getMessage());
            throw new RuntimeException("Error while updating ActiveStatusDefinedBy: " + e.getMessage());
        }
    }
    private void callUpdateOrderItemMethod(String orderItemId, OrderUpdateDto orderUpdateDto) {
        try {
            ResponseEntity<GeneralResponse<OrderItemDto>> response =
                    this.orderRequestServiceClient.updateOrderItem(GeneralOrderRequest.builder()
                                    .orderItemId(orderItemId)
                                    .orderUpdateDto(orderUpdateDto)
                            .build());

        } catch (Exception e) {
            log.error("Error while updating ActiveStatusDefinedBy:" + e.getMessage());
            throw new RuntimeException("Error while updating ActiveStatusDefinedBy: " + e.getMessage());
        }
    }

    private OrderUpdateDto getOrderUpdateDto(String activityId, String activityName, String eventName) {
        OrderStatusDto activeStatus = new OrderStatusDto();
        activeStatus.setId(UUID.randomUUID());
        activeStatus.setState(activityId);
        activeStatus.setSubState("Created");
        activeStatus.setDescription(activityName);
        activeStatus.setEventName(eventName);
        activeStatus.setStartDate(LocalDateTime.now());
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setActiveStatusDefinedBy(activeStatus);
        return orderUpdateDto;
    }
}