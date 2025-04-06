package com.tahaakocer.orderservice.controller;

import com.tahaakocer.orderservice.dto.update.OrderUpdateDto;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.model.mongo.BaseOrder;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final OrderUpdateStrategy orderUpdateStrategy;
    private final OrderRequestRepository orderRequestRepository;

    public DemoController(OrderUpdateStrategy orderUpdateStrategy, OrderRequestRepository orderRequestRepository) {
        this.orderUpdateStrategy = orderUpdateStrategy;
        this.orderRequestRepository = orderRequestRepository;
    }

    @PostMapping("/demo")
    public String demo(@RequestBody OrderUpdateDto orderUpdateDto) {
        OrderRequest orderRequest = orderRequestRepository.findById(UUID.fromString("0aebd21b-2f67-4639-afc9-93633200dd84")).get();
        boolean a = orderUpdateStrategy.objectStatus(orderRequest);

        if(orderUpdateStrategy.canHandle(orderUpdateDto))
        {
            if(orderUpdateStrategy.objectStatus(orderRequest))
            orderUpdateStrategy.update(orderRequest, orderUpdateDto);
            else orderUpdateStrategy.create(orderRequest, orderUpdateDto);
        }
        return a ? "true" : "false";
    }
}
