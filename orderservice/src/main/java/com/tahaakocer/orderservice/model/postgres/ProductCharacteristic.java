package com.tahaakocer.orderservice.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "product_characteristic")
@Data
public class ProductCharacteristic {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "characteristic_id", nullable = false)
    private UUID characteristicId;

}
