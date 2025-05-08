package com.tahaakocer.crm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "party_role")
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class PartyRole extends BaseEntity {

    @OneToMany(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountRef> accountRefs = new ArrayList<>();

    @OneToMany(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementRef> agreementRefs = new ArrayList<>();

    @OneToMany(mappedBy = "partyRole",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactMedium> contactMedia = new ArrayList<>();

    @OneToOne(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer customer;

    @OneToOne(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private Partner partner;

    @OneToOne(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private Individual individual;

    @OneToMany(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Characteristic> characteristics = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_type_ref_id")
    private RoleTypeRef roleTypeRef;

}
