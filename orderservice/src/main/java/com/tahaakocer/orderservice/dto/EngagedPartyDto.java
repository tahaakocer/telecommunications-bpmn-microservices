package com.tahaakocer.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EngagedPartyDto extends BaseDto{

    private Integer tckn;
    private String firstName;
    private String lastName;
    private Integer phoneNumber;
    private String email;
    private String keycloakUserId;
    private String formattedAddress;
    private Integer bbk;
}
