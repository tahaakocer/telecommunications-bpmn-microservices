package com.tahaakocer.commondto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class OrderItemDto extends BaseDto {

    private String code;

    private String processInstanceId;

    private BpmnFlowRefDto bpmnFlowRef;

    private OrderStatusDto activeStatusDefinedBy;

    private List<CharacteristicDto> characteristics;

    private OrderRequestRefDto orderRequestRef;

    private AccountRefDto accountRef;

    private Boolean flowEnded;

    private ProductDto product;

    private String orderType;
    private String type;
}
