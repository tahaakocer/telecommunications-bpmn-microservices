package com.tahaakocer.orderservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCatalogRequest {

    private String code;
    private String name;
    private String specificationCode;
    private String productType;
    private String productConfType;

}
