package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderItem extends BaseModel {

    @Field("code")
    private String code;

    @Field("characteristics")
    private List<Characteristic> characteristics;

    @Field("account_ref")
    @JsonProperty("account_ref")
    private AccountRef accountRef;

    @Field("product")
    private Product product;

    @Field("bpmn_flow_ref")
    @JsonProperty("bpmn_flow_ref")
    private BpmnFlowRef bpmnFlowRef;

    @Field("order_item_type")
    @JsonProperty("order_item_type")
    private String orderItemType;

    @PrePersist
    public void prePersist() {
        this.code = Util.generateRandomCode("ORDIT");
    }
}