package com.tahaakocer.orderservice.controller;

import com.tahaakocer.orderservice.dto.BpmnFlowRefDto;
import com.tahaakocer.orderservice.dto.initializer.InitializerDto;
import com.tahaakocer.orderservice.dto.order.OrderRequestDto;
import com.tahaakocer.orderservice.dto.response.OrderRequestResponse;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.dto.update.FieldUpdateRequest;
import com.tahaakocer.orderservice.dto.update.MultiFieldUpdateRequest;
import com.tahaakocer.orderservice.dto.update.OrderUpdateDto;
import com.tahaakocer.orderservice.mapper.OrderRequestMapper;
import com.tahaakocer.orderservice.service.OrderRequestService;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/initialize")
@Slf4j
public class InitializeController {
    private final OrderRequestService orderRequestService;
    private final OrderRequestMapper orderRequestMapper;

    public InitializeController(OrderRequestService orderRequestService, OrderRequestMapper orderRequestMapper) {
        this.orderRequestService = orderRequestService;
        this.orderRequestMapper = orderRequestMapper;
    }

    @PostMapping("/{orderType}/create-order-request")
    public ResponseEntity<GeneralResponse<OrderRequestResponse>> createOrderRequest(
            @RequestBody InitializerDto initializerDto,
            @RequestParam String channel,
            @NotBlank(message = "Order type is required") @PathVariable String orderType) {
        OrderRequestResponse orderRequestResponse =
                this.orderRequestService.createOrderRequest(initializerDto, orderType, channel);
        return ResponseEntity.ok(GeneralResponse.<OrderRequestResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Order request created successfully")
                .data(orderRequestResponse)
                .build());
    }

    @PostMapping("/{orderRequestId}/update-order-request")
    public ResponseEntity<GeneralResponse<OrderRequestResponse>> updateOrderRequest(
            @PathVariable UUID orderRequestId,
            @RequestBody OrderUpdateDto orderUpdateDto
            ) {
        log.info("Received request to update order {}", orderRequestId);

        OrderRequestResponse updatedOrder = this.orderRequestService.updateOrderRequest(orderRequestId, orderUpdateDto);

        return ResponseEntity.ok(GeneralResponse.<OrderRequestResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Order updated successfully")
                .data(updatedOrder)
                .build()
        );
    }

    @PutMapping("/{orderRequestId}/update-field")
    public ResponseEntity<GeneralResponse<OrderRequestDto>> updateField(
            @PathVariable UUID orderRequestId,
            @RequestBody FieldUpdateRequest request) {

        log.info("Received request to update field {} in order {}", request.getFieldPath(), orderRequestId);

        OrderRequestDto updatedOrder = this.orderRequestService.updateOrderField(
                orderRequestId,
                request.getFieldPath(),
                request.getValue()
        );

        return ResponseEntity.ok(GeneralResponse.<OrderRequestDto>builder()
                .code(HttpStatus.OK.value())
                .message("Field updated successfully")
                .data(updatedOrder)
                .build()
        );
    }

    @PutMapping("/{orderRequestId}/batch-update")

    public ResponseEntity<GeneralResponse<OrderRequestDto>> updateMultipleFields(
            @PathVariable UUID orderRequestId,
            @RequestBody MultiFieldUpdateRequest request) {
        log.info("Received request to update multiple fields in order {}", orderRequestId);

        OrderRequestDto updatedOrder = this.orderRequestService.updateOrderFields(
                orderRequestId,
                request.getFieldsToUpdate()
        );


        return ResponseEntity.ok(GeneralResponse.<OrderRequestDto>builder()
                .code(HttpStatus.OK.value())
                .message("Field updated successfully")
                .data(updatedOrder)
                .build()
        );
    }

    @PutMapping("/{orderRequestId}/update-products")
    public ResponseEntity<GeneralResponse<OrderRequestDto>> updateProducts(
            @PathVariable UUID orderRequestId,
            @RequestParam String productCatalogCode,
            @RequestParam(defaultValue = "false") boolean willBeDelete) {
        log.info("Received request to update products in order {}", orderRequestId);
        OrderRequestDto updatedOrder = this.orderRequestService.updateProduct(
                orderRequestId,
                productCatalogCode,
                willBeDelete
        );

        return ResponseEntity.ok(GeneralResponse.<OrderRequestDto>builder()
                .code(HttpStatus.OK.value())
                .message("Field updated successfully")
                .data(updatedOrder)
                .build()
        );
    }

    @PutMapping("/{orderRequestId}/update-order-status")
    public ResponseEntity<GeneralResponse<OrderRequestResponse>> updateOrderStatus(
            @PathVariable UUID orderRequestId,
            @RequestBody BpmnFlowRefDto bpmnFlowRefDto
            ) {
        log.info("Received request to update order status in order {}", orderRequestId);
        OrderRequestResponse updatedOrder = this.orderRequestService.updateOrderStatus(
                orderRequestId, bpmnFlowRefDto
        );

        return ResponseEntity.ok(GeneralResponse.<OrderRequestResponse>builder()
                .code(HttpStatus.OK.value())
                .message("BpmnFlowRef updated successfully")
                .data(updatedOrder)
                .build()
        );
    }


    @GetMapping("/{orderRequestId}/get-order-request")
    public ResponseEntity<GeneralResponse<OrderRequestDto>> getOrderRequest(
            @PathVariable UUID orderRequestId
    ) {
        log.info("Received request to get order {}", orderRequestId);
        OrderRequestDto orderRequestResponse = this.orderRequestService.getOrderRequest(orderRequestId);
        return ResponseEntity.ok(GeneralResponse.<OrderRequestDto>builder()
                .code(HttpStatus.OK.value())
                .message("Order request retrieved successfully")
                .data(orderRequestResponse)
                .build()
        );
    }
}
