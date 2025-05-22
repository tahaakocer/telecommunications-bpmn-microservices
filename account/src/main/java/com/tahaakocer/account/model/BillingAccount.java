package com.tahaakocer.account.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "billing_account", schema = "account_management")
@AllArgsConstructor
@NoArgsConstructor
public class BillingAccount extends BaseEntity{

    private String billCycle; //TUMAY MULTI15

    @OneToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(mappedBy = "billingAccount", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private BillingSystem billingSystem;

}
