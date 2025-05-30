package com.tahaakocer.agreement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "product", schema = "agreement_management")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//@AttributeOverride(name = "id", column = @Column(name = "id"))
public class Product extends BaseEntity {
//    @Id
//    @Column(name = "id")
//    private UUID id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductCharacteristic> productCharacteristics;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private AgreementItem agreementItem;

    private String code;

    private String name;

    private String productType;

    private String productConfType;

    private boolean mainProduct;

    private boolean mandatoryAddon;

}
