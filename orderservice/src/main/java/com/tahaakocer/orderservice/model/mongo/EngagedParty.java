package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EngagedParty extends BaseModel {

    @Field("tckn")
    private Long tckn;

    @Field("first_name")
    @JsonProperty("first_name")
    private String firstName;

    @Field("last_name")
    @JsonProperty("last_name")
    private String lastName;

    @Field("phone_number")
    @JsonProperty("phone_number")
    private Long phoneNumber;

    @Field("email")
    private String email;

    @Field("keycloak_user_id")
    @JsonProperty("keycloak_user_id")
    private String keycloakUserId;

    @Field("formatted_address")
    @JsonProperty("formatted_address")
    private String formattedAddress;

    @Field("bbk")
    private Integer bbk;


}
