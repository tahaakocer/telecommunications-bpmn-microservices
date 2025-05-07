package com.tahaakocer.crm.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class PartnerRegisterResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String keycloakUserId;

}
