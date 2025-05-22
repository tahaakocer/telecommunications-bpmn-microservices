package com.tahaakocer.account.repository;

import com.tahaakocer.account.model.BillingSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillingSystemRepository extends JpaRepository<BillingSystem, UUID> {
}
