package com.tahaakocer.camunda.dto.orderRequestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class OrderRequestResponse extends BaseDto {

    private String code;
    private String channel;
    private BaseOrderDto baseOrder;

}
