package com.tahaakocer.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BpmnFlowRefDto {

    @JsonProperty("process_instance_id")
    private String processInstanceId;

    @JsonProperty("process_definition_name")
    private String processDefinitionName;

    @JsonProperty("current_activity_id")
    private String currentActivityId;

    @JsonProperty("current_activity_name")
    private String currentActivityName;
}
