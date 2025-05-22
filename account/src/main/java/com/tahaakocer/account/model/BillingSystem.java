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
@Table(name = "billing_system", schema = "account_management")
@AllArgsConstructor
@NoArgsConstructor
public class BillingSystem extends BaseEntity{

    private String billingSystemCode;
    private String billingSystemId;

    @OneToOne(fetch = jakarta.persistence.FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_account_id")
    private BillingAccount billingAccount;

    @PrePersist
    public void prePersist(){
        if(billingSystemCode == null) {
            billingSystemCode = "BILLING_SYSTEM_" + System.currentTimeMillis();
        }
        if(billingSystemId == null) {
            billingSystemId = UUID.randomUUID().toString();
        }

    }
}
