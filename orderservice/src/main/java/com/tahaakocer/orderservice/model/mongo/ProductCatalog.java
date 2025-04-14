package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document("productCatalog")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCatalog extends BaseModel{
    @Field("code")
    private String code;

    @Field("name")
    private String name;

    @Field("specifications")
    private List<Specification> specifications;

    @Field("productType")
    private String productType;

    @Field("productConfType")
    private String productConfType;


    @PrePersist
    public void prePersist() {
        if(this.code == null) {
            this.code = Util.generateRandomCode("PRD");
        }
    }
}
