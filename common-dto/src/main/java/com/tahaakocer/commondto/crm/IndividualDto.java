package com.tahaakocer.commondto.crm;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class IndividualDto {
    private UUID id;

    private IndividualIdentificationDto individualIdentification;

//    private PartyRoleDto partyRole;

    private String firstName;
    private String lastName;
    private String formattedName;
    private Integer birthYear;

}
