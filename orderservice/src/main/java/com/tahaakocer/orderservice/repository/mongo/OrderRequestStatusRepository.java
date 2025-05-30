package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.OrderRequestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRequestStatusRepository extends MongoRepository<OrderRequestStatus, UUID> {
    Optional<OrderRequestStatus> findByOrderRequestRef_OrderRequestId(UUID orderRequestRefId);

}
