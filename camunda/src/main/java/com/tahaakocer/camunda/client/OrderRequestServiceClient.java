package com.tahaakocer.camunda.client;

import com.tahaakocer.camunda.dto.GeneralResponse;

import com.tahaakocer.commondto.order.BpmnFlowRefDto;
import com.tahaakocer.commondto.order.OrderItemDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "orderservice",url = "${services.order-request-service.url}")
public interface OrderRequestServiceClient {


    @PostMapping("/api/initialize/{orderRequestId}/update-order-request")
    ResponseEntity<GeneralResponse<OrderRequestResponse>> updateOrderRequest(
            @PathVariable UUID orderRequestId,
            @RequestBody OrderUpdateDto orderUpdateDto
    );

    @PutMapping("/api/initialize/{orderRequestId}/update-order-status")
    ResponseEntity<GeneralResponse<OrderRequestResponse>> updateOrderStatus(
            @PathVariable UUID orderRequestId,
            @RequestBody BpmnFlowRefDto bpmnFlowRefDto
    );
    @GetMapping("/api/initialize/{orderRequestId}/get-order-request")
    ResponseEntity<GeneralResponse<OrderRequestDto>> getOrderRequest(
            @PathVariable UUID orderRequestId
    );
    @PostMapping("/update-order-item")
    public ResponseEntity<GeneralResponse<OrderItemDto>> updateOrderItem(@RequestBody GeneralOrderRequest generalOrderRequest);
}
