package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
public class OrderRequestRef{
    @Id
    private UUID id;
    @Field("orderRequestId")
    private UUID orderRequestId;
    @Field("code")
    private String code;
    @Field("orderType")
    private String orderType;
    @Field("channel")
    private String channel;
}
