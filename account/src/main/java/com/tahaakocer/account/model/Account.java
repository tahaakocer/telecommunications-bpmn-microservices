package com.tahaakocer.account.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "account", schema = "account_management")
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity{
    private String accountCode;
    private String reasor; //new, relocation, etc.
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactMedium> contactMedia = new ArrayList<>();

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private PartyRoleRef partyRoleRef;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private BillingAccountSacInfo billingAccountSacInfo;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Characteristic> characteristics = new ArrayList<>();

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private BillingAccount billingAccount;

    @PrePersist
    public void prePersist() {
        if (accountCode == null || accountCode.isEmpty()) {
            accountCode = "ACCT-" + System.currentTimeMillis();
        }

    }
}
