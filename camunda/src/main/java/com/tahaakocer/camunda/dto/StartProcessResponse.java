package com.tahaakocer.camunda.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartProcessResponse {
    private String processInstanceId;
    private String businessKey;
    private String processDefinitionId;
    private String errorMessage;
}
