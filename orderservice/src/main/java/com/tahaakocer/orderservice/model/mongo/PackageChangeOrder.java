package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageChangeOrder extends BaseOrder{
    @Field("source_product")
    @JsonProperty("source_product")
    private Product sourceProduct;
    @Field("target_product")
    @JsonProperty("target_product")
    private Product targetProduct;
    @Field("re_commitment")
    @JsonProperty("re_commitment")
    private String reCommitment;
}
