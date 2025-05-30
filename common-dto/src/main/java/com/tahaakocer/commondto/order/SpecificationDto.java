package com.tahaakocer.commondto.order;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.tahaakocer.commondto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecificationDto extends BaseDto {
    private String code;
    private List<CharacteristicDto> characteristics;
}
