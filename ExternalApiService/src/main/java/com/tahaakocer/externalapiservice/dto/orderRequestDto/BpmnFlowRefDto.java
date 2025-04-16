package com.tahaakocer.externalapiservice.dto.orderRequestDto;

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

    private String processDefinitionId;
    private String processBusinessKey;


}
