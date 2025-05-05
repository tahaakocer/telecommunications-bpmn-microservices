package com.tahaakocer.orderservice.repository.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "individual_identification")
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class IndividualIdentification extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "individual_id")
    private Individual individual;

    @Column(name = "identification_id")
    private String identificationId;
}
