package com.tahaakocer.crm.repository;

import com.tahaakocer.crm.model.AccountRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRefRepository extends JpaRepository<AccountRef, UUID> {
}
