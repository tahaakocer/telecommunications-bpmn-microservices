package com.tahaakocer.orderservice.dto.order;

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
@SuperBuilder

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestDto {
    private String code;
    private String channel;
    private BaseOrderDto baseOrder;
}
