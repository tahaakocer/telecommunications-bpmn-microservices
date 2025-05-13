package com.tahaakocer.crm.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "contact_medium")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class ContactMedium extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_medium_characteristic_id")
    private ContactMediumCharacteristic contactMediumCharacteristic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;

    private String type;
}