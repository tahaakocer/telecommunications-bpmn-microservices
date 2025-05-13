package com.tahaakocer.crm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.crm.model.PartyRole;
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
public class CharacteristicDto {

    private PartyRoleDto partyRole;
    private String name;
    private String value;
}
