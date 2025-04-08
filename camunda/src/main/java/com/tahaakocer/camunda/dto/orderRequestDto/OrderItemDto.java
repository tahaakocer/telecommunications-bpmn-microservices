package com.tahaakocer.camunda.dto.orderRequestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto extends BaseDto {
    private String code;
    private List<CharacteristicDto> characteristics;
    private AccountRefDto accountRef;
    private ProductDto product;
    private BpmnFlowRefDto bpmnFlowRef;
    private String orderItemType;
}
