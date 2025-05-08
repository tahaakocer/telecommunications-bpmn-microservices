package com.tahaakocer.crm.dto;

import com.tahaakocer.crm.model.Partner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PartnerUserDto {
    private PartnerDto partner;

    private Long tckn;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private Long phoneNumber;
    private String email;
    private String keycloakUserId;
}
