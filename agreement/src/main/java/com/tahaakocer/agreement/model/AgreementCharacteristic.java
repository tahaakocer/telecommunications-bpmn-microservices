package com.tahaakocer.agreement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "agreement_characteristic", schema = "agreement_management")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AgreementCharacteristic extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    private String name;
    private String value;
}
