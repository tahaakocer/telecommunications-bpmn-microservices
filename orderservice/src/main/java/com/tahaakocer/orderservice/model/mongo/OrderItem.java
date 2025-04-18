package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("orderItem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseModel {

    @Field("code")
    private String code;

    @Field("characteristics")
    private List<Characteristic> characteristics;

    @Field("accountRef")
    private AccountRef accountRef;

    @Field("product")
    private Product product;

    @Field("bpmnFLowRef")
    private BpmnFlowRef bpmnFlowRef;

    @Field("orderRequestRef")
    private OrderRequestRef orderRequestRef;

    @Field("orderItemType")
    private String orderItemType;

    @PrePersist
    public void prePersist() {
        this.code = Util.generateRandomCode("ORDIT");
    }
}