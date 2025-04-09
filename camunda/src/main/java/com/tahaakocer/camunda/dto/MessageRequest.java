package com.tahaakocer.camunda.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    private String messageName;

    private Map<String, Object> variables = new HashMap<>();
}