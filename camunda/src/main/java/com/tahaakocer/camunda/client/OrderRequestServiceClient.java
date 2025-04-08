package com.tahaakocer.camunda.client;

import com.tahaakocer.camunda.dto.GeneralResponse;
import com.tahaakocer.camunda.dto.orderRequestDto.BpmnFlowRefDto;
import com.tahaakocer.camunda.dto.orderRequestDto.OrderRequestResponse;
import com.tahaakocer.camunda.dto.orderRequestDto.OrderUpdateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "orderservice",url = "${services.order-request-service.url}")
public interface OrderRequestServiceClient {


    @PatchMapping("/{orderRequestId}/update-order-request")
    ResponseEntity<GeneralResponse<OrderRequestResponse>> updateOrderRequest(
            @PathVariable UUID orderRequestId,
            @RequestBody OrderUpdateDto orderUpdateDto
    );

    @PutMapping("/api/initialize/{orderRequestId}/update-order-status")
    ResponseEntity<GeneralResponse<OrderRequestResponse>> updateOrderStatus(
            @PathVariable UUID orderRequestId,
            @RequestBody BpmnFlowRefDto bpmnFlowRefDto
    );
}
