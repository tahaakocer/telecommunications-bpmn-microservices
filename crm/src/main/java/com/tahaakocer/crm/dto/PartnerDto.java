package com.tahaakocer.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PartnerDto {
    private UUID id;
    private String tckn;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String phoneNumber;
    private String email;
    private String password;
    private String keycloakUserId;

}
