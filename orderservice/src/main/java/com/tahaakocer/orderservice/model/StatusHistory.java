package com.tahaakocer.orderservice.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusHistory {
    private UUID id;
    private String state;
    private String subState;
    private String description;
    private String eventName;
    private LocalDateTime startDate;

}
