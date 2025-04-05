package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCatalog extends BaseModel{
    @Field("code")
    private String code;

    @Field("name")
    private String name;

    @Field("specification")
    private Specification specification;

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
