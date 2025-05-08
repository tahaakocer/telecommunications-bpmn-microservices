package com.tahaakocer.crm.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "account_ref")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AccountRef extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;

    private String accountCode;

    private UUID refAccountId;

    private LocalDateTime endDate;

}
