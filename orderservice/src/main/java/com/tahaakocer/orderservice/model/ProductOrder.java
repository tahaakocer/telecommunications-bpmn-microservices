package com.tahaakocer.orderservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder extends BaseOrder{
    @DBRef
    private List<Product> products = new ArrayList<>();

}
