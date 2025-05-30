package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.Addon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddonRepository extends MongoRepository<Addon, UUID> {


    List<Addon> findByMainProductId(UUID uuid);
}
