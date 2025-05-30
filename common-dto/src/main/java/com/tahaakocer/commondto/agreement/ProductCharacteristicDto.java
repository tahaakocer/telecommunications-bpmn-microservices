package com.tahaakocer.commondto.agreement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCharacteristicDto extends BaseDto {

//    private Product product;

    private String code;
    private String name;
    private Object value;
    private String sourceType;
}
