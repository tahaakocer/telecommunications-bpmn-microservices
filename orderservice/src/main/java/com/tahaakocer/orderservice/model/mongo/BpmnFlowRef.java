package com.tahaakocer.orderservice.model.mongo;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
@Builder
public class BpmnFlowRef {
    @Id
    private UUID id;

    @Field("process_instance_id")
    private String processInstanceId;

    @Field("process_definition_name")
    private String processDefinitionName;

    @Field("current_activity_id")
    private String currentActivityId;

    @Field("current_activity_name")
    private String currentActivityName;
}