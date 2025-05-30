package com.tahaakocer.agreement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "agreement_item_status", schema = "agreement_management")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AgreementItemStatus extends BaseEntity{

    @OneToOne(mappedBy = "agreementItemStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private AgreementItem agreementItem;

    private boolean pending;

    private String status;

    private String statusReason;

}
