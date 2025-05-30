package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.OrderItemStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemStatusRepository extends MongoRepository<OrderItemStatus, String> {
    Optional<OrderItemStatus> findByOrderItemRef_OrderItemId(UUID orderItemId);

}
