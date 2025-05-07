package com.tahaakocer.crm.controller;

import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.crm.dto.CustomerDto;
import com.tahaakocer.crm.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<CustomerDto>> createCustomer(@RequestBody GeneralOrderRequest generalOrderRequest) {
        CustomerDto customerDto = this.customerService.createCustomer(generalOrderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<CustomerDto>builder()
                .code(200)
                .message("Customer created successfully")
                .data(customerDto)
                .build()
        );

    }
}
