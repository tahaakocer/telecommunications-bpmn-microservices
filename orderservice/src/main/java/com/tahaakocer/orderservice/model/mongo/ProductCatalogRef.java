package com.tahaakocer.orderservice.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
public class ProductCatalogRef {
    @Id
    private UUID id;

    @Field("product_catalog_code")
    private String productCatalogCode;

    @Field("ref_product_catalog_id")
    private String refProductCatalogId;
}
