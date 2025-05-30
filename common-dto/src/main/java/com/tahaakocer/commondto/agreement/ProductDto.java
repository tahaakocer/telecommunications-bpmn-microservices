package com.tahaakocer.commondto.agreement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto extends BaseDto {

    private AgreementItemDto agreementItem;

    private List<ProductCharacteristicDto> productCharacteristics;

    private String code;
    private String name;
    private String productType;
    private String productConfType;
    private boolean mainProduct;
}
