package com.tahaakocer.orderservice.model;

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

    @Field("firstName")
    private String firstName;

    @Field("lastName")
    private String lastName;

    @Field("birthYear")
    private Integer birthYear;

    @Field("phoneNumber")
    private Long phoneNumber;

    @Field("email")
    private String email;

    @Field("keycloakUserId")
    private String keycloakUserId;

    @Field("address")
    private Address address;

    @Field("formattedAddress")
    private String formattedAddress;

    @Field("bbk")
    private Integer bbk;
}
