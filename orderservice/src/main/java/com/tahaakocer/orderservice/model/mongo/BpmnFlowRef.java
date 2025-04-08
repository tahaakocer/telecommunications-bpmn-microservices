package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BpmnFlowRef {
    @Id
    private UUID id;

    @Field("processInstanceId")
    private String processInstanceId;

    @Field("processDefinitionId")
    private String processDefinitionId;

    @Field("processBusinessKey")
    private String processBusinessKey;
}