package com.tahaakocer.crm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@Entity
@Table(name = "partner")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Partner extends BaseEntity{

    private UUID id;
    private String tcNo;
    private String firstName;
    private String lastName;
    private String birthYear;
    private String phoneNumber;
    private String email;
    private String keycloakUserId;
}
