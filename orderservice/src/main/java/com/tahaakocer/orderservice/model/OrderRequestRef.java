package com.tahaakocer.orderservice.model;

import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderRequestRef{
    @Id
    private UUID id;

    private UUID orderRequestId;

    private String code;

    private LocalDateTime orderDate;

    private String orderType;

    private BpmnFlowRef bpmnFlowRef;

    private String channel;

    private Boolean isDraft;
}
