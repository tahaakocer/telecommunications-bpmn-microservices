package com.tahaakocer.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class BaseDto {
    private UUID id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String lastModifiedBy;
}
