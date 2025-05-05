package com.tahaakocer.orderservice.repository.postgres;

import jakarta.mail.Part;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class Customer extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private PartyRole partyRole;
}
