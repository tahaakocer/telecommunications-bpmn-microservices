package com.tahaakocer.orderservice.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tahaakocer.orderservice.dto.ActiveStatusDefinedByDto;
import com.tahaakocer.orderservice.dto.BaseDto;
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
public class OrderRequestDto extends BaseDto {
    private String code;
    private String channel;
    private BaseOrderDto baseOrder;
    private ActiveStatusDefinedByDto activeStatusDefinedBy;

}
