package com.tahaakocer.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "characteristic")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Characteristic extends BaseModel {

    @Field("code")
    private String code;
    @Field("name")
    private String name;
    @Field("value")
    private Object value;
    @Field("sourceType")
    private String sourceType;

}

