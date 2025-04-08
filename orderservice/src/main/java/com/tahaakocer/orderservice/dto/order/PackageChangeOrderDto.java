package com.tahaakocer.orderservice.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.orderservice.model.mongo.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder

public class PackageChangeOrderDto extends BaseOrderDto {
    private Product sourceProduct;
    private Product targetProduct;
    private String reCommitment;
}
