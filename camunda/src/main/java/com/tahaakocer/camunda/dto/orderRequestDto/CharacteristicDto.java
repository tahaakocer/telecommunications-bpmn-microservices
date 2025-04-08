package com.tahaakocer.camunda.dto.orderRequestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicDto extends BaseDto {

    private String code;
    private String name;
    private Object value;
    private String sourceType;

}
