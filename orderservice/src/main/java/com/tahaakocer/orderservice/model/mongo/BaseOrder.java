package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductOrder.class),
        @JsonSubTypes.Type(value = PackageChangeOrder.class),

})
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseOrder extends BaseModel {

    @Field("code")
    @JsonProperty("code")
    private String code;

    @Field("engaged_party")
    @JsonProperty("engaged_party")
    private EngagedParty engagedParty;

    @Field("accounts")
    @JsonProperty("account_refs")
    private List<AccountRef> accountRefs = new ArrayList<>();

    @Field("bpmn_flow_ref")
    @JsonProperty("bpmn_flow_ref")
    private BpmnFlowRef bpmnFlowRef;

    @Field("order_items")
    @JsonProperty("order_items")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Field("characteristics")
    @JsonProperty("characteristics")
    private List<Characteristic> characteristics = new ArrayList<>();

    @Field("order_type")
    @JsonProperty("order_type")
    private String orderType;

    @Field("is_draft")
    @JsonProperty("is_draft")
    private Boolean isDraft;


    @PrePersist
    public void prePersist() {
        this.code = Util.generateRandomCode("ORD");
    }
}