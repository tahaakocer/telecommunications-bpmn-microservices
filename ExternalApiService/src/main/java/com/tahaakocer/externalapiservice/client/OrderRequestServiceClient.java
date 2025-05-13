package com.tahaakocer.externalapiservice.client;

import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import com.tahaakocer.externalapiservice.config.FeignClientConfig;
import com.tahaakocer.externalapiservice.dto.GeneralResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
