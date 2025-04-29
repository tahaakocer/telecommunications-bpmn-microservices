package com.tahaakocer.commondto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class OrderUpdateDto {
    private EngagedPartyDto engagedParty;
    private List<AccountRefDto> accountRefs;
    private BpmnFlowRefDto bpmnFlowRef;
    private List<BaseOrderItemDto> orderItems;
    private List<CharacteristicDto> characteristics;
    private Boolean isDraft;
    private AddressDto address;

    private List<ProductDto> products;
    private ProductDto sourceProduct;
    private ProductDto targetProduct;
    private String reCommitment;
    private OrderStatusDto activeStatusDefinedBy;

    private Boolean removeUnlistedCharacteristics = false;
    private Boolean removeUnlistedAccountRefs = false;
    private Boolean removeUnlistedOrderItems = false;
    private Boolean removeUnlistedProducts = false;

    public boolean isRemoveUnlistedCharacteristics() {
        return removeUnlistedCharacteristics != null && removeUnlistedCharacteristics;
    }

    public boolean isRemoveUnlistedAccountRefs() {
        return removeUnlistedAccountRefs != null && removeUnlistedAccountRefs;
    }

    public boolean isRemoveUnlistedOrderItems() {
        return removeUnlistedOrderItems != null && removeUnlistedOrderItems;
    }

    public boolean isRemoveUnlistedProducts() {
        return removeUnlistedProducts != null && removeUnlistedProducts;
    }
}
