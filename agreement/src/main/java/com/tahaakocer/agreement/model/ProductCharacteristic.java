package com.tahaakocer.agreement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "product_characteristic", schema = "agreement_management")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//@AttributeOverride(name = "id", column = @Column(name = "id"))
public class ProductCharacteristic extends BaseEntity {
//    @Id
//    @Column(name = "id")
//    private UUID id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;

    private String code;
    private String name;

    @Column(columnDefinition = "text")
    private String value;

    private String sourceType;
}
