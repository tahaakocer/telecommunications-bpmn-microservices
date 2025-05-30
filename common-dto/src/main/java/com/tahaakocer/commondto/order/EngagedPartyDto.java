package com.tahaakocer.commondto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
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
