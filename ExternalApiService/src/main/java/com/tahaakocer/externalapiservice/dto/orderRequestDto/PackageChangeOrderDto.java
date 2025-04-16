package com.tahaakocer.externalapiservice.dto.orderRequestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageChangeOrderDto extends BaseOrderDto {
    private ProductDto sourceProduct;
    private ProductDto targetProduct;
    private String reCommitment;
}
