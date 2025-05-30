package com.tahaakocer.orderservice;

import com.tahaakocer.orderservice.model.OrderRequest;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import org.junit.jupiter.api.Test;

public class BaseOrderRequestDtoServiceTest {
    private final OrderRequestRepository orderRequestRepository;

    public BaseOrderRequestDtoServiceTest(OrderRequestRepository orderRequestRepository) {
        this.orderRequestRepository = orderRequestRepository;
    }

    @Test
    public void save() {
        OrderRequest orderRequest = OrderRequest.builder()
                .code("123")
                .build();

        this.orderRequestRepository.save(orderRequest);
    }
}
