package com.tahaakocer.orderservice.dto.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tahaakocer.orderservice.dto.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderUpdateDto {
    @JsonProperty("engaged_party")
    private EngagedPartyDto engagedParty;
    @JsonProperty("account_refs")
    private List<AccountRefDto> accountRefs;
    @JsonProperty("bpmn_flow_ref")
    private BpmnFlowRefDto bpmnFlowRef;
    @JsonProperty("order_items")
    private List<OrderItemDto> orderItems;
    @JsonProperty("characteristics")
    private List<CharacteristicDto> characteristics;
    @JsonProperty("is_draft")
    private Boolean isDraft;



    @JsonProperty("products")
    private List<ProductDto> products;
    @JsonProperty("source_product")
    private ProductDto sourceProduct;
    @JsonProperty("target_product")
    private ProductDto targetProduct;
    @JsonProperty("re_commitment")
    private String reCommitment;
}
