package com.tahaakocer.orderservice.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderItemRef {
    @Id
    private UUID id;

    private UUID orderItemId;

    private String code;

}
