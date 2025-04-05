package com.tahaakocer.orderservice.model.mongo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
public class EngagedParty extends BaseModel {

    @Field("tckn")
    private Integer tckn;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("phone_number")
    private Integer phoneNumber;

    @Field("email")
    private String email;

    @Field("keycloak_user_id")
    private String keycloakUserId;

    @Field("formatted_address")
    private String formattedAddress;

    @Field("bbk")
    private Integer bbk;


}
