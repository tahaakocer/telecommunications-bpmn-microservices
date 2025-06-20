package com.tahaakocer.orderservice.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tahaakocer.orderservice.utils.Util;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
    private String code;

    @Field("engagedParty")
    private EngagedParty engagedParty;

    @Field("accountRef")
    private AccountRef accountRef;

    @Field("bpmnFlowRef")
    private BpmnFlowRef bpmnFlowRef;

    @Field("characteristics")
    private List<Characteristic> characteristics = new ArrayList<>();

    @Field("orderType")
    private String orderType;

    @Field("orderItems")
    @DBRef
    private List<BaseOrderItem> orderItems = new ArrayList<>();

    @Field("partyRoleRef")
    private PartyRoleRef partyRoleRef;

    @Field("isDraft")
    private Boolean isDraft;

    @Field("agreementRef")
    private AgreementRef agreementRef;


    @PrePersist
    public void prePersist() {
        this.code = Util.generateRandomCode("ORD");
    }
}