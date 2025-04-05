package com.tahaakocer.orderservice.dto.response;

import com.tahaakocer.orderservice.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicGetAllResponse extends BaseDto {
    private String code;
    private String name;
    private Object value;
    private String sourceType;
}
