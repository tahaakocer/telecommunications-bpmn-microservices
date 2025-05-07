package com.tahaakocer.crm.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity{

    private String accountCode;

    private String accountName;

    @PrePersist
    public void prePersist() {
        if (accountCode == null || accountCode.isEmpty()) {
            accountCode = "ACCT-" + System.currentTimeMillis();
        }
    }
}
