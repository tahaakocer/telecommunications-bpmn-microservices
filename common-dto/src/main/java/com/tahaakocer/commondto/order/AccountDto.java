package com.tahaakocer.commondto.order;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
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
public class AccountDto extends BaseDto {
    private String accountName;
    private String accountCode;
    private String formattedBillAddress;

    private OrderRequestRefDto orderRequestRef;
}
