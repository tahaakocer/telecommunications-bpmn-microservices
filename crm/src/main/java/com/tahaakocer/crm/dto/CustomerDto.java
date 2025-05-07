package com.tahaakocer.crm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.order.BaseDto;
import com.tahaakocer.crm.model.PartyRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDto extends BaseDto {
    private PartyRoleDto partyRole;
    private UUID partyRoleId;

    private String keycloakUserId;

    private Boolean hasCommunicationPermAppr;

    private Boolean hasPersonalDataUsagePerm;
}
