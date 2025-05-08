package com.tahaakocer.crm.model;

import jakarta.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;

    @OneToOne(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private PartnerUser partnerUser;

    private Boolean hasCommunicationPermAppr;
    private Boolean hasPersonalDataUsagePerm;

}
