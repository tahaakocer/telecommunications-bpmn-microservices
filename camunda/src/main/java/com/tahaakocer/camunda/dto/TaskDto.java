package com.tahaakocer.camunda.dto;

import lombok.*;
import org.camunda.bpm.engine.task.DelegationState;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    private String id;
    private String processInstanceId;
    private String processDefinitionId;
    private String name;
    private String taskDefinitionKey;
    private String taskState;
    private String executionId;
    private Date createTime;
}
