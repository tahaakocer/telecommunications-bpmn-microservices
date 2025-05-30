package com.tahaakocer.commondto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import lombok.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor // Parametresiz yapıcı
@AllArgsConstructor
public class GeneralOrderRequest {
    private String orderRequestId;
    private String orderItemId;

    private OrderUpdateDto update;

}
