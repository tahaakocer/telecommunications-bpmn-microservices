package com.tahaakocer.commondto.order;

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
@SuperBuilder
public class BaseDto {
    private UUID id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String lastModifiedBy;
}
