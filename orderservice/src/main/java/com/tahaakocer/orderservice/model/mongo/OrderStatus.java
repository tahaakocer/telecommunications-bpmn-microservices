package com.tahaakocer.orderservice.model.mongo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderStatus {
    @Id
    private UUID id;
    private String state;
    private String subState;
    private String description;
    private String eventName;
    private LocalDateTime startDate;

}
