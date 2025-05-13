package com.tahaakocer.crm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "characteristic")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Characteristic extends BaseEntity {

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;

    private String name;

    private String value;

}
