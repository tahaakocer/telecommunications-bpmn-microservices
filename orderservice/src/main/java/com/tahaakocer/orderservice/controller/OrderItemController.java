package com.tahaakocer.orderservice.controller;

import com.tahaakocer.commondto.order.OrderItemDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.service.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/itemize-order")
    public ResponseEntity<GeneralResponse<OrderRequestResponse>> itemizeOrder(@RequestBody GeneralOrderRequest generalOrderRequest) {

        OrderRequestResponse orderRequestResponse =
                this.orderItemService.itemizeOrder(generalOrderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<OrderRequestResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Order itemized successfully")
                .data(orderRequestResponse)
                .build()
        );
    }
    @PostMapping("/update-order-item")
    public ResponseEntity<GeneralResponse<OrderItemDto>> updateOrderItem(@RequestBody GeneralOrderRequest generalOrderRequest) {
        OrderItemDto orderItemDto =
                this.orderItemService.updateOrderItem(
                        UUID.fromString(generalOrderRequest.getOrderItemId()), generalOrderRequest.getOrderUpdateDto());
        return ResponseEntity.ok(GeneralResponse.<OrderItemDto>builder()
                .code(HttpStatus.OK.value())
                .message("Order item updated successfully")
                .data(orderItemDto)
                .build()
        );
    }

}
