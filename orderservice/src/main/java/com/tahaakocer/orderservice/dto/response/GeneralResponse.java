package com.tahaakocer.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponse<T> {
    private int code;
    private String message;
    private T data;
}
