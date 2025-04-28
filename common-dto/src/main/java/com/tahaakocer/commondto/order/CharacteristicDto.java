package com.tahaakocer.commondto.order;

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
public class CharacteristicDto extends BaseDto {

    private String code;
    private String name;
    private Object value;
    private String sourceType;

}
