package com.tahaakocer.account.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "party_role_ref", schema = "account_management")
@AllArgsConstructor
@NoArgsConstructor
public class PartyRoleRef extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    private UUID refPartyRoleId;
}
