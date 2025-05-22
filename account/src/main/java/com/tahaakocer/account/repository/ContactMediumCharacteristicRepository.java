package com.tahaakocer.account.repository;

import com.tahaakocer.account.model.ContactMediumCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactMediumCharacteristicRepository extends JpaRepository<ContactMediumCharacteristic, UUID> {
}
