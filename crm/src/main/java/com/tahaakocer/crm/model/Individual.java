package com.tahaakocer.crm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "individual")
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class Individual {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "individual", cascade = CascadeType.ALL, orphanRemoval = true)
    private IndividualIdentification individualIdentification;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;

    private String firstName;
    private String lastName;
    private String formattedName;
    private Integer birthYear;


}
