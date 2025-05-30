package com.tahaakocer.commondto.crm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
import com.tahaakocer.commondto.order.PartyRoleRefDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto extends BaseDto {
    private String accountCode;
    private String reasor;
    private List<ContactMediumDto> contactMedia = new ArrayList<>();

    private PartyRoleRefDto partyRoleRef;

    private BillingAccountSacInfoDto billingAccountSacInfo;

    private List<AccountCharacteristicDto> characteristics = new ArrayList<>();
    private BillingAccountDto billingAccount;

}
