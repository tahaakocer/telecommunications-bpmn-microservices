package com.tahaakocer.crm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer", schema = "party_role_management")
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class Customer extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;

    private String keycloakUserId;

    private Boolean hasCommunicationPermAppr;

    private Boolean hasPersonalDataUsagePerm;
}
