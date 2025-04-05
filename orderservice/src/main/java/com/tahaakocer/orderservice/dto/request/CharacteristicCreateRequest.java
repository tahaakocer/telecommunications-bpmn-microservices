package com.tahaakocer.orderservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicCreateRequest {
    private String code;
    private String name;
    private Object value;
    private String sourceType;
}
