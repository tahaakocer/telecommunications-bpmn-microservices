package com.tahaakocer.orderservice.model.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "individual")
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class Individual {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id")
    private PartyRole partyRole;


}
