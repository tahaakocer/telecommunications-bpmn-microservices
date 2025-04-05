package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRequestRepository extends MongoRepository<OrderRequest, UUID> {
}
