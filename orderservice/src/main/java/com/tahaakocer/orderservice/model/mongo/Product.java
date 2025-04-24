package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "product")
public class Product extends BaseModel {

    @Field("code")
    private String code;

    @Field("mainProductCode")
    private String mainProductCode;

    @Field("name")
    private String name;

    @Field("characteristics")
    private List<Characteristic> characteristics;

    @Field("productCatalogId")
    private UUID productCatalogId;

    @Field("productType")
    private String productType;

    @Field("productConfType")
    private String productConfType;

    private OrderRequestRef orderRequestRef;

    @PrePersist
    public void prePersist() {
        if(this.code == null) {
            this.code = Util.generateRandomCode("PRD");
        }
    }

}