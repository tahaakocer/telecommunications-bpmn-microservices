package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.mongo.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends MongoRepository<Product, UUID> {
    Optional<Product> findByMainProductCode(String productCatalogCode);
}
