package com.tahaakocer.orderservice.dto.request;

import com.tahaakocer.orderservice.dto.SpecificationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCatalogRequest {

    private String code;
    private String name;
    private List<SpecificationDto> specifications;
    private String productType;
    private String productConfType;

}
