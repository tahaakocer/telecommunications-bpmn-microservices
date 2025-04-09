package com.tahaakocer.camunda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private String processInstanceId;
    private String businessKey;
    private String messageName;
    private String executionId;

}
