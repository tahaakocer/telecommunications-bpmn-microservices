package com.tahaakocer.orderservice.dto;

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
public class EngagedPartyDto extends BaseDto{

    @JsonProperty("tckn")
    private Long tckn;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("phone_number")
    private Long phoneNumber;
    private String email;
    @JsonProperty("keycloak_user_id")
    private String keycloakUserId;
    @JsonProperty("formatted_address")
    private String formattedAddress;
    @JsonProperty("bbk")
    private Integer bbk;
}
