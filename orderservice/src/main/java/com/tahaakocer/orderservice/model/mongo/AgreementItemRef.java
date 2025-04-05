package com.tahaakocer.orderservice.model.mongo;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
public class AgreementItemRef {
    @Id
    private UUID id;

    @Field("ref_agreement_id")
    private UUID refAgreementId;

    @Field("name")
    private String name;

}
