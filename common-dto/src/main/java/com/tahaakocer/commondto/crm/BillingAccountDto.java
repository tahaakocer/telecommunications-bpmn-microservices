package com.tahaakocer.commondto.crm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.order.BaseDto;
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
public class BillingAccountDto extends BaseDto {
    private String billCycle; //TUMAY MULTI15
    private AccountDto account;

    private BillingSystemDto billingSystem;
}
