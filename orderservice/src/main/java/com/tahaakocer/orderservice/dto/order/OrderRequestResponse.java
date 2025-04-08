package com.tahaakocer.orderservice.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tahaakocer.orderservice.dto.BaseDto;
import lombok.*;
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
