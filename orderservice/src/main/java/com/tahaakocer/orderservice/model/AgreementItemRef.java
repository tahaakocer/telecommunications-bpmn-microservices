package com.tahaakocer.orderservice.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
public class AgreementItemRef {
    @Id
    private UUID id;

    @Field("refAgreementItemId")
    private UUID refAgreementItemId;

    @Field("refAgreementId")
    private UUID refAgreementId;

    @Field("name")
    private String name;

}
