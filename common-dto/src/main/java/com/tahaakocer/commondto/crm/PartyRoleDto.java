package com.tahaakocer.commondto.crm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.order.BaseDto;

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
public class PartyRoleDto extends BaseDto {

    private List<AccountRefDto> accountRefs;


    private List<AgreementRefDto> agreementRefs;

    private List<ContactMediumDto> contactMedia;

    private CustomerDto customer;
    private PartnerDto partner;

    private IndividualDto individual;

    private List<PartyRoleCharacteristicDto> characteristics = new ArrayList<>();

    private RoleTypeRefDto roleTypeRef;

}
