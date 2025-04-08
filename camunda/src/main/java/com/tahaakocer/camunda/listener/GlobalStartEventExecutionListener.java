package com.tahaakocer.camunda.listener;

import com.tahaakocer.camunda.client.OrderRequestServiceClient;
import com.tahaakocer.camunda.dto.GeneralResponse;
import com.tahaakocer.camunda.dto.orderRequestDto.BpmnFlowRefDto;
import com.tahaakocer.camunda.dto.orderRequestDto.OrderRequestResponse;
import com.tahaakocer.camunda.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class GlobalStartEventExecutionListener implements ExecutionListener {
    private final OrderRequestServiceClient orderRequestServiceClient;

    protected GlobalStartEventExecutionListener(OrderRequestServiceClient orderRequestServiceClient) {
        this.orderRequestServiceClient = orderRequestServiceClient;
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();
        String processDefinitionId = execution.getProcessDefinitionId();
        String processBusinessKey = execution.getProcessBusinessKey();

        String orderRequestId = (String) execution.getVariable("orderRequestId");

        if (orderRequestId == null || orderRequestId.isEmpty()) {
            log.warn("GlobalStartEventExecutionListener - orderRequestId is null or empty for processInstanceId: {}",
                    processInstanceId);
            throw new GeneralException("orderRequestId is null or empty for processInstanceId: " + processInstanceId);
        }

        log.info("GlobalStartEventExecutionListener - processInstanceId: {}" +
                        "\nprocessDefinitionId: {}" +
                        "\nprocessBusinessKey: {}",
                processInstanceId, processDefinitionId, processBusinessKey);

        BpmnFlowRefDto bpmnFlowRefDto = new BpmnFlowRefDto(
                null,
                processInstanceId,
                processDefinitionId,
                processBusinessKey
        );

        try {
            ResponseEntity<GeneralResponse<OrderRequestResponse>> responseEntity =
                    orderRequestServiceClient.updateOrderStatus(UUID.fromString(orderRequestId), bpmnFlowRefDto);

            if (responseEntity == null) {
                log.error("Response entity is null");
                throw new GeneralException("Update request failed - null response");
            }

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("BpmnFlowRef updated successfully for orderRequestId: {}", orderRequestId);
            } else {

                log.error("BpmnFlowRef update failed with status: {} and response: {}",
                        responseEntity.getStatusCode(),
                        responseEntity.getBody());
                throw new GeneralException("BpmnFlowRef update failed with status: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            log.error("BpmnFlowRef update failed for orderRequestId: {}", orderRequestId, e);
            throw new GeneralException("BpmnFlowRef update failed: " + e.getMessage(), e);
        }
    }
}