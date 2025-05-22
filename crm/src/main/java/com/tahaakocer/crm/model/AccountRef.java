package com.tahaakocer.crm.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "account_ref", schema = "party_role_management")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@ToString(exclude = "partyRole")
@EqualsAndHashCode(callSuper = true, exclude = "partyRole")
public class AccountRef extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;

    private String accountCode;

    private UUID refAccountId;

}
