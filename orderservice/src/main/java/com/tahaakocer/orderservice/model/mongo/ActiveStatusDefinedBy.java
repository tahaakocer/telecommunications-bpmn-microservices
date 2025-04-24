package com.tahaakocer.orderservice.model.mongo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActiveStatusDefinedBy {
    @Id
    private UUID id;
    private String state;
    private String subState;
    private String description;
    private String eventName;
    private LocalDateTime startDate;
}
