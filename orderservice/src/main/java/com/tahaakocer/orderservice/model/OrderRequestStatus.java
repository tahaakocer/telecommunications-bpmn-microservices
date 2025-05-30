package com.tahaakocer.orderservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document("orderRequestStatusHistory")
public class OrderRequestStatus extends OrderStatus {
    private OrderRequestRef orderRequestRef;
    private List<StatusHistory> orderRequestStatusHistory;
}
