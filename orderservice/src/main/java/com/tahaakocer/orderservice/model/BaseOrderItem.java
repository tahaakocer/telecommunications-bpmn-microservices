package com.tahaakocer.orderservice.model;

import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("orderItems")

public class BaseOrderItem extends BaseModel {

    private String code;

    private String processInstanceId;

    private BpmnFlowRef bpmnFlowRef;

    private OrderStatus activeStatusDefinedBy;

    private List<Characteristic> characteristics;

    private OrderRequestRef orderRequestRef;

    private AccountRef accountRef;

    private Boolean flowEnded;

    @DBRef
    private Product product;

    private String orderType;

    private AgreementItemRef agreementItemRef;

    @PrePersist
    public void prePersist() {
        this.code = Util.generateRandomCode("ORDIT");
    }
}