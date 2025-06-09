package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.OrderRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRequestRepository extends MongoRepository<OrderRequest, UUID> {

    @Query("{'baseOrder.engagedParty.tckn': ?0}")
    Optional<OrderRequest> findByTckn(Long tckn);

}
