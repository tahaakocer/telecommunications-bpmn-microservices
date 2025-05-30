package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.ProductCatalog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCatalogRepository extends MongoRepository<ProductCatalog,UUID> {
    boolean existsByCode(String code);

    Optional<ProductCatalog> findByCode(String code);

    @Query(value = "{ 'code' : { $regex: ?0, $options: 'i' } }", fields = "{ 'code': 1, 'name': 1, '_id': 0 }")
    List<ProductCatalog> findByProductCatalogCodeAndNameContainingIgnoreCase(String query);


    List<ProductCatalog> findByProductConfType(String productConfType);
}
