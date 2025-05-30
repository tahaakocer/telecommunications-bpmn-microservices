package com.tahaakocer.agreement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "party_role_ref", schema = "agreement_management")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PartyRoleRef extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    private UUID refPartyRoleId;
}
