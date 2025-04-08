package com.tahaakocer.orderservice.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.orderservice.dto.ProductDto;
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
public class ProductOrderDto extends BaseOrderDto{

    private List<ProductDto> products;


}
