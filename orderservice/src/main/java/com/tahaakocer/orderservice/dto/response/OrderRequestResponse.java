package com.tahaakocer.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.orderservice.dto.BaseDto;
import com.tahaakocer.orderservice.dto.order.BaseOrderDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class OrderRequestResponse extends BaseDto {

    private String code;
    private String channel;
    private UUID orderRequestId;
    private String processInstanceId;
    private String processDefinitionId;
    private String processBusinessKey;

}
