package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Product extends BaseModel {

    @Field("code")
    private String code;

    @Field("main_product_code")
    @JsonProperty("main_product_code")
    private String mainProductCode;

    @Field("name")
    private String name;

    @Field("characteristics")
    private List<Characteristic> characteristics;

    @Field("product_catalog_id")
    @JsonProperty("product_catalog_id")
    private UUID productCatalogId;

    @Field("product_type")
    @JsonProperty("product_type")
    private String productType;

    @Field("product_conf_type")
    @JsonProperty("product_conf_type")
    private String productConfType;


    @PrePersist
    public void prePersist() {
        if(this.code == null) {
            this.code = Util.generateRandomCode("PRD");
        }
    }

}