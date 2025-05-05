package com.tahaakocer.orderservice.model.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "agreement_ref")
@AllArgsConstructor
@NoArgsConstructor
public class AgreementRef extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;

}
