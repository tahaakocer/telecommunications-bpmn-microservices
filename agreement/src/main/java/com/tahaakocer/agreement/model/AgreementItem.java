package com.tahaakocer.agreement.model;

import com.tahaakocer.agreement.util.ItemNumberGenerator;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "agreement_item", schema = "agreement_management")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AgreementItem extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private AgreementItemStatus agreementItemStatus;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_ref_id")
    private AgreementItemAccountRef agreementItemAccountRef;

    private LocalDateTime endDateTime;
    private LocalDateTime startDateTime;

    @Column(unique = true)
    private Integer itemNumber;

    @PrePersist
    public void initializeItemNumber() {
        if (this.itemNumber == null) {
            this.itemNumber = ItemNumberGenerator.getNext();
        }
    }

    private Integer version = 0;

    @PreUpdate
    public void updateVersion() {
        this.version++;
    }
}
