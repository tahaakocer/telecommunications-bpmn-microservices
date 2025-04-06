package com.tahaakocer.orderservice.dto;

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
public class OrderItemDto extends BaseDto{
    private String code;
    private List<CharacteristicDto> characteristics;

    @JsonProperty("account_ref")
    private AccountRefDto accountRef;

    private ProductDto product;

    @JsonProperty("bpmn_flow_ref")
    private BpmnFlowRefDto bpmnFlowRef;

    @JsonProperty("order_item_type")
    private String orderItemType;
}
