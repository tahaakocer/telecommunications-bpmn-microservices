package com.tahaakocer.commondto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class GeneralOrderRequest {
    private String orderRequestId;
    private String orderItemId;

    private OrderUpdateDto orderUpdateDto;

}
