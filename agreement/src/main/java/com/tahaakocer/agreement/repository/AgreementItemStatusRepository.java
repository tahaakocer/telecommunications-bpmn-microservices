package com.tahaakocer.agreement.repository;

import com.tahaakocer.agreement.model.AgreementItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgreementItemStatusRepository extends JpaRepository<AgreementItemStatus, UUID> {
}
