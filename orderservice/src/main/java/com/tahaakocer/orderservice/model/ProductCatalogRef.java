package com.tahaakocer.orderservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
public class ProductCatalogRef {
    @Id
    private UUID id;

    @Field("productCatalogCode")
    private String productCatalogCode;

    @Field("refProductCatalogId")
    private String refProductCatalogId;
}
