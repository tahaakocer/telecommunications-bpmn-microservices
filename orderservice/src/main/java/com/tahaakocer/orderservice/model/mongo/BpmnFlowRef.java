package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmnFlowRef {
    @Id
    private UUID id;

    @Field("process_instance_id")
    @JsonProperty("process_instance_id")
    private String processInstanceId;

    @Field("process_definition_name")
    @JsonProperty("process_definition_name")
    private String processDefinitionName;

    @Field("current_activity_id")
    @JsonProperty("current_activity_id")
    private String currentActivityId;

    @Field("current_activity_name")
    @JsonProperty("current_activity_name")
    private String currentActivityName;
}