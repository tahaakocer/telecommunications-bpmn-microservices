package com.tahaakocer.account.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "billing_account_sac_info", schema = "account_management")
@AllArgsConstructor
@NoArgsConstructor
public class BillingAccountSacInfo extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private Integer maxSpeed;
    private String SVUID;
    private boolean adslPortState;
    private Integer adslDistance;
    private boolean vdslPortState;
    private Integer vdslDistance;
    private boolean fiberPortState;
    private Integer fiberDistance;
}
