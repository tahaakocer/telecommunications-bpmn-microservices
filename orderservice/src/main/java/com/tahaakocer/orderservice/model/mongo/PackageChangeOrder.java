package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageChangeOrder extends BaseOrder{
    @Field("sourceProduct")
    private Product sourceProduct;
    @Field("targetProduct")
    private Product targetProduct;
    @Field("reCommitment")
    private String reCommitment;
}
