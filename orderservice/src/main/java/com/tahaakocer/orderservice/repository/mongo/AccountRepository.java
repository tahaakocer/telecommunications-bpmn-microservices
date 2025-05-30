package com.tahaakocer.orderservice.repository.mongo;

import com.tahaakocer.orderservice.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends MongoRepository<Account, UUID> {
    Optional<Account> findByAccountCode(String accountCode);

    Optional<Account> findByOrderRequestRef_Id(UUID orderRequestRefId);
}
