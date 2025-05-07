package com.tahaakocer.crm.repository;

import com.tahaakocer.crm.model.PartyRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartyRoleRepository extends JpaRepository<PartyRole, UUID> {
}
