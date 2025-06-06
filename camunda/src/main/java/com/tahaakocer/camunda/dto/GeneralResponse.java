package com.tahaakocer.camunda.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class GeneralResponse<T> {
    private int code;
    private String message;
    private T data;
}
