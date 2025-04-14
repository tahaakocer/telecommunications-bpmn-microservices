package com.tahaakocer.orderservice.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@Document("account")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Account extends BaseModel {
    @Field("accountName")
    private String accountName;
    @Field("accountCode")
    private String accountCode;
    @Field("formattedBillAddress")
    private String formattedBillAddress;

    @Field("orderRequestRef")
    private OrderRequestRef orderRequestRef;
}
