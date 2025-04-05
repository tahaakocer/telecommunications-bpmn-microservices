package com.tahaakocer.orderservice.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldUpdateRequest {
    private String fieldPath;
    private Object value;
}