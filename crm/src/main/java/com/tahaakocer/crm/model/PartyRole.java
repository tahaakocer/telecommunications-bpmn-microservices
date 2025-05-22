package com.tahaakocer.crm.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "party_role", schema = "party_role_management")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@ToString(exclude = {"accountRefs", "agreementRefs", "contactMedia", "customer", "partner", "individual", "characteristics"})
@EqualsAndHashCode(callSuper = true, exclude = {"accountRefs", "agreementRefs", "contactMedia", "customer", "partner", "individual", "characteristics"})

public class PartyRole extends BaseEntity {

    @OneToMany(mappedBy = "partyRole")
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
