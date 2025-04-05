package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.mongo.Specification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpecificationRepository extends MongoRepository<Specification, UUID> {
    Optional<Specification> findByCode(String name);

    boolean existsByCode(String code);
}
