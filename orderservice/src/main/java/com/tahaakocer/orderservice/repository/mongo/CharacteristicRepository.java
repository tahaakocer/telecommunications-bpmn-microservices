package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.Characteristic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CharacteristicRepository extends MongoRepository<Characteristic, UUID> {
    boolean existsByCode(String code);

    Optional<Characteristic> findByCode(String code);
}
