package com.tahaakocer.camunda.dto.orderRequestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EngagedPartyDto extends BaseDto {

    private Long tckn;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private Long phoneNumber;
    private String email;
    private String keycloakUserId;
    private AddressDto address;
    private String formattedAddress;
    private Integer bbk;
}
