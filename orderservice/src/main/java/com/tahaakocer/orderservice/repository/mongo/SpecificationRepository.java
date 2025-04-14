package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.mongo.Specification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecificationRepository extends MongoRepository<Specification, UUID> {
    Optional<Specification> findByCode(String name);

    boolean existsByCode(String code);

    @Query(value = "{ 'code' : { $regex: ?0, $options: 'i' } }", fields = "{ 'code': 1, '_id': 0 }")
    List<Specification> findBySpecificationsCodeContainingIgnoreCase(String query);
}
