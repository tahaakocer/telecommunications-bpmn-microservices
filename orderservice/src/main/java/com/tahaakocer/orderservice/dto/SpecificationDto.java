package com.tahaakocer.orderservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecificationDto extends BaseDto {
    private String code;
    private List<CharacteristicDto> characteristics;
}
