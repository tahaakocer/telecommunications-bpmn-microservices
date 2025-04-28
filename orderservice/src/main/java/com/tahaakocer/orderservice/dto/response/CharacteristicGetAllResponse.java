package com.tahaakocer.orderservice.dto.response;

import com.tahaakocer.commondto.order.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CharacteristicGetAllResponse extends BaseDto {
    private String code;
    private String name;
    private Object value;
    private String sourceType;
}
