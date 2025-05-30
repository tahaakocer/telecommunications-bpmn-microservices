package com.tahaakocer.commondto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
