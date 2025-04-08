package com.tahaakocer.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder

public class OrderItemDto extends BaseDto{
    private String code;
    private List<CharacteristicDto> characteristics;

    private AccountRefDto accountRef;

    private ProductDto product;

    private BpmnFlowRefDto bpmnFlowRef;
    private String orderItemType;
}
