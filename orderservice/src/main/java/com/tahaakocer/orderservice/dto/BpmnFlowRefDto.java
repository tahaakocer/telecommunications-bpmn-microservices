package com.tahaakocer.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BpmnFlowRefDto {
    private UUID id;
    private String processInstanceId;
    private String processDefinitionName;
    private String currentActivityId;
    private String currentActivityName;
}
