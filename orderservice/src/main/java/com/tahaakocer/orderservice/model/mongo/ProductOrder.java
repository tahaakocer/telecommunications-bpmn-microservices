package com.tahaakocer.orderservice.model.mongo;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder extends BaseOrder{
    @Field("products")
    private List<Product> products = new ArrayList<>();

    @Field("orderItems")
    private List<ProductOrderItem> orderItems = new ArrayList<>();
}
