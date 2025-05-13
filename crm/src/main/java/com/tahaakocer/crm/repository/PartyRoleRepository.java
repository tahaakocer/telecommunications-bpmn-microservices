package com.tahaakocer.crm.repository;

import com.tahaakocer.crm.model.PartyRole;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartyRoleRepository extends JpaRepository<PartyRole, UUID> {


    @Query("SELECT pr FROM PartyRole pr JOIN pr.characteristics c WHERE c.name = 'orderRequestId' AND c.value = :orderRequestId")
    Optional<PartyRole> findByOrderRequestIdCharacteristic(@Param("orderRequestId") String orderRequestId);
}
