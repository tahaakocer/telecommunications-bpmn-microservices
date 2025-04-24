package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

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
