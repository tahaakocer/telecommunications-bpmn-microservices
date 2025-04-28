package com.tahaakocer.commondto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder

public class ProductDto extends BaseDto {
    private String code;
    private String mainProductCode;
    private String name;
    private List<CharacteristicDto> characteristics;
    private UUID productCatalogId;
    private String productType;
    private String productConfType;
    private OrderRequestRefDto orderRequestRef;


}
