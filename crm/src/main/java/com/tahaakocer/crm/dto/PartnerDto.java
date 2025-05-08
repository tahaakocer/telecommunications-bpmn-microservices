package com.tahaakocer.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PartnerDto {
    private PartyRoleDto partyRole;

    private PartnerUserDto partnerUser;

    private Boolean hasCommunicationPermAppr;
    private Boolean hasPersonalDataUsagePerm;

}
