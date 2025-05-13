package com.tahaakocer.crm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "agreement")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Agreement extends BaseEntity{

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementItem> agreementItems;

}
