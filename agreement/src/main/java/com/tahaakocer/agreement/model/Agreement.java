package com.tahaakocer.agreement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "agreement", schema = "agreement_management")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Agreement extends BaseEntity {

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<AgreementCharacteristic> agreementCharacteristics = new ArrayList<>();

    @OneToOne(mappedBy = "agreement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private PartyRoleRef partyRoleRef;

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<AgreementItem> agreementItems = new ArrayList<>();

    private LocalDateTime agreementPeriodEndDateTime;
    private LocalDateTime agreementPeriodStartDateTime;
    private String description;
    private String name;
    private String type;
    private Integer version = 0;

    @PreUpdate
    public void updateVersion() {
        this.version++;
    }

    @PrePersist
    public void prePersist() {
        if (this.name == null) {
            this.name = "AGREEMENT_" + System.currentTimeMillis();
        }
        if (this.description == null) {
            this.description = type + "Agreement" + name;
        }
    }

}
