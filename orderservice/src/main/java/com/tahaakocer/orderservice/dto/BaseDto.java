package com.tahaakocer.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDto {
    private UUID id;
    @JsonProperty("create_date")
    private LocalDateTime createDate;
    @JsonProperty("update_date")
    private LocalDateTime updateDate;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("last_modified_by")
    private String lastModifiedBy;
}
