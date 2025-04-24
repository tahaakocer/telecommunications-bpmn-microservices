package com.tahaakocer.camunda.repository;

import com.tahaakocer.camunda.model.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessRepository extends JpaRepository<ProcessEntity, UUID> {
    Optional<ProcessEntity> findByProcessKey(String processKey);

    List<ProcessEntity> findByOrderType(String orderType);
}
