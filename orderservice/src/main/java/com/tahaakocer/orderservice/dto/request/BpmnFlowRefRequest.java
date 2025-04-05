package com.tahaakocer.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BpmnFlowRefRequest {
    private String processInstanceId;
    private String processDefinitionName;
    private String currentActivityId;
    private String currentActivityName;
}
