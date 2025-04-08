package com.tahaakocer.orderservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CharacteristicCreateResponse {
    private String code;
    private String name;
    private Object value;
    private String sourceType;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String lastModifiedBy;
}
