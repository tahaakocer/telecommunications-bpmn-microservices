package com.tahaakocer.account.repository;

import com.tahaakocer.account.model.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, UUID> {
}
