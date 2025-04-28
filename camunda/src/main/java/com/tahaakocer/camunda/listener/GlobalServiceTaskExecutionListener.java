package com.tahaakocer.camunda.listener;

import com.tahaakocer.camunda.client.OrderRequestServiceClient;
import com.tahaakocer.camunda.dto.GeneralResponse;
import com.tahaakocer.camunda.dto.orderRequestDto.ActiveStatusDefinedByDto;
import com.tahaakocer.camunda.dto.orderRequestDto.OrderRequestResponse;
import com.tahaakocer.camunda.dto.orderRequestDto.OrderUpdateDto;
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

        // orderRequestId değişkenini process instance'dan çekelim
        String orderRequestId = (String) execution.getVariable("orderRequestId");

        ActiveStatusDefinedByDto activeStatus = new ActiveStatusDefinedByDto();
        activeStatus.setId(UUID.randomUUID());
        activeStatus.setState(activityId);
        activeStatus.setSubState("Created");
        activeStatus.setDescription(activityName);
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