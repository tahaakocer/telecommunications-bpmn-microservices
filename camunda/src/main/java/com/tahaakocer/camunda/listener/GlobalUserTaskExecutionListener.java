package com.tahaakocer.camunda.listener;

import com.tahaakocer.camunda.client.OrderRequestServiceClient;
import com.tahaakocer.camunda.dto.GeneralResponse;
import com.tahaakocer.camunda.dto.orderRequestDto.OrderRequestResponse;
import com.tahaakocer.camunda.dto.orderRequestDto.OrderStatusDto;
import com.tahaakocer.camunda.dto.orderRequestDto.OrderUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class GlobalUserTaskExecutionListener implements TaskListener {
    private final OrderRequestServiceClient orderRequestServiceClient;

    public GlobalUserTaskExecutionListener(OrderRequestServiceClient orderRequestServiceClient) {
        this.orderRequestServiceClient = orderRequestServiceClient;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String taskId = delegateTask.getId();
        String taskName = delegateTask.getName();
        String processInstanceId = delegateTask.getProcessInstanceId();
        String executionId = delegateTask.getExecutionId();
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String eventName = delegateTask.getEventName();

        log.info("GlobalUserTaskExecutionListener - taskId: {}" +
                        "\ntaskName: {}" +
                        "\nprocessInstanceId: {}" +
                        "\nexecutionId: {}" +
                        "\ntaskDefinitionKey: {}" +
                        "\neventName: {}",
                taskId, taskName, processInstanceId, executionId, taskDefinitionKey, eventName);

        // orderRequestId değişkenini process instance'dan çekelim
        String orderRequestId = (String) delegateTask.getVariable("orderRequestId");

        OrderStatusDto activeStatus = new OrderStatusDto();
        activeStatus.setId(UUID.randomUUID());
        activeStatus.setState(taskId);
        activeStatus.setSubState("Created");
        activeStatus.setDescription(taskName);
        activeStatus.setEventName(eventName);
        activeStatus.setStartDate(LocalDateTime.now());
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setActiveStatusDefinedBy(activeStatus);

        try {
            ResponseEntity<GeneralResponse<OrderRequestResponse>> response =
                    this.orderRequestServiceClient.updateOrderRequest(UUID.fromString(orderRequestId), orderUpdateDto);
        } catch (Exception e) {
            log.error("Error while updating ActiveStatusDefinedBy:" + e.getMessage());
            throw new RuntimeException("Error while updating ActiveStatusDefinedBy: " + e.getMessage());
        }
    }


}