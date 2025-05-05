package com.tahaakocer.orderservice.repository.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "contact_medium")
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class ContactMedium extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_medium_characteristic_id")
    private ContactMediumCharacteristic contactMediumCharacteristic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;
}
