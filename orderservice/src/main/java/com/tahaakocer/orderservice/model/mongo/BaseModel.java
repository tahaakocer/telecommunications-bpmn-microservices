package com.tahaakocer.orderservice.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseModel {

    @Id
    private UUID id = UUID.randomUUID();

    @CreatedDate
    @Field("createDate")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Field("updateDate")
    private LocalDateTime updateDate;

    @CreatedBy
    @Field("createdBy")
    private String createdBy;

    @LastModifiedBy
    @Field("lastModifiedBy")
    private String lastModifiedBy;
}
