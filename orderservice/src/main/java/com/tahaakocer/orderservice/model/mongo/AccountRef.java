package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
public class AccountRef {

    @Id
    private UUID id;

    @Field("accountId")
    private UUID accountId;

    @Field("accountCode")
    private String accountCode;


}