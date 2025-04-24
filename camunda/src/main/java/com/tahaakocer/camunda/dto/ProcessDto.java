package com.tahaakocer.camunda.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ProcessDto {
    private UUID id;
    private String processKey;
    private String processName;
    private String orderType;
    private boolean starter;
}
