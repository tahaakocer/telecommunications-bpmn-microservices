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
@Document("OrderItemStatusHistory")
public class OrderItemStatus extends OrderStatus {
    private OrderItemRef orderItemRef;
    private List<StatusHistory> orderItemStatusHistory;
}
