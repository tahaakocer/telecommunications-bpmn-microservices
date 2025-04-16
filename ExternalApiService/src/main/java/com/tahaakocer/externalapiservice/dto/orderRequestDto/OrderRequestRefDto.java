package com.tahaakocer.externalapiservice.dto.orderRequestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestRefDto {

    private UUID id;
    private UUID orderRequestId;
    private String code;
    private String orderType;
    private String channel;
}
