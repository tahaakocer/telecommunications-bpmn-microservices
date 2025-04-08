package com.tahaakocer.orderservice.model.mongo;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
public class AgreementItemRef {
    @Id
    private UUID id;

    @Field("refAgreementId")
    private UUID refAgreementId;

    @Field("name")
    private String name;

}
