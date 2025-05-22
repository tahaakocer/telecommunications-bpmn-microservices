package com.tahaakocer.commondto.crm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.order.AccountDto;
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
public class BillingAccountSacInfoDto extends BaseDto {
    private AccountDto account;
    private Integer maxSpeed;
    private String SVUID;
    private boolean adslPortState;
    private Integer adslDistance;
    private boolean vdslPortState;
    private Integer vdslDistance;
    private boolean fiberPortState;
    private Integer fiberDistance;

}
