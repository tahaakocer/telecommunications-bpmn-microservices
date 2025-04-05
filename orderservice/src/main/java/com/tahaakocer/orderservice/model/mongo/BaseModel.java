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
    @Field("create_date")
    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Field("update_date")
    @JsonProperty("update_date")
    private LocalDateTime updateDate;

    @CreatedBy
    @Field("created_by")
    @JsonProperty("created_by")
    private String createdBy;

    @LastModifiedBy
    @Field("last_modified_by")
    @JsonProperty("last_modified_by")
    private String lastModifiedBy;
}
