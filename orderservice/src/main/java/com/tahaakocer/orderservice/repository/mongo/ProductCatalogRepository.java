package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.mongo.ProductCatalog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCatalogRepository extends MongoRepository<ProductCatalog,UUID> {
    boolean existsByCode(String code);

    Optional<ProductCatalog> findByCode(String code);
}
