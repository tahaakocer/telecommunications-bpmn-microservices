package com.tahaakocer.orderservice.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageChangeOrderItem extends BaseOrderItem {
    private String type = PackageChangeOrderItem.class.getName();
}
