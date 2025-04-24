package com.tahaakocer.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.orderservice.model.mongo.BpmnFlowRef;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class OrderRequestRefDto {

    private UUID id;

    private UUID orderRequestId;

    private String code;

    private LocalDateTime orderDate;

    private String orderType;

    private BpmnFlowRefDto bpmnFlowRef;

    private String channel;

    private Boolean isDraft;
}
