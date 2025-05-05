package com.tahaakocer.orderservice.model.postgres;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@Entity
@MappedSuperclass
@NoArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
}
