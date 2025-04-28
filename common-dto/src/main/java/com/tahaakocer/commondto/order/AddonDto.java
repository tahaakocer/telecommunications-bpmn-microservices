package com.tahaakocer.commondto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AddonDto extends BaseDto {

    private UUID mainProductId;
    private UUID addonProductId;
    private ProductCatalogDto addonProduct;
    private boolean mandatory;

}
