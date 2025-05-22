package com.tahaakocer.account.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "contact_medium", schema = "account_management")
@AllArgsConstructor
@NoArgsConstructor
public class ContactMedium extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_medium_characteristic_id")
    private ContactMediumCharacteristic contactMediumCharacteristic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String type;
}
