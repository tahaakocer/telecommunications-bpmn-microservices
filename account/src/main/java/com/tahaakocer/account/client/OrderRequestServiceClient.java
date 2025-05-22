package com.tahaakocer.account.client;

import com.tahaakocer.account.config.FeignClientConfig;
import com.tahaakocer.commondto.order.OrderItemDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.commondto.response.OrderRequestResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "orderservice",
        configuration = FeignClientConfig.class
)
public interface OrderRequestServiceClient {


    @PostMapping("/api/initialize/{orderRequestId}/update-order-request")
    ResponseEntity<GeneralResponse<OrderRequestResponse>> updateOrderRequest(
            @PathVariable UUID orderRequestId,
            @RequestBody OrderUpdateDto orderUpdateDto
    );

    @GetMapping("/api/initialize/{orderRequestId}/get-order-request")
    ResponseEntity<GeneralResponse<OrderRequestDto>> getOrderRequest(
            @PathVariable UUID orderRequestId
    );
    @PostMapping("/api/order-item/update-order-item")
    ResponseEntity<GeneralResponse<OrderItemDto>> updateOrderItem(
            @RequestBody GeneralOrderRequest generalOrderRequest
    );
}
