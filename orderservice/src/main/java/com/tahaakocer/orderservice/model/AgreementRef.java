package com.tahaakocer.orderservice.model;

import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
public class AgreementRef {
    @Id
    private UUID id;

    private UUID refAgreementId;
}
