package com.tahaakocer.agreement.repository;

import com.tahaakocer.agreement.model.AgreementCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgreementCharacteristicRepository extends JpaRepository<AgreementCharacteristic, UUID> {
}
