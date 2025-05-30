package com.tahaakocer.agreement.repository;

import com.tahaakocer.agreement.model.PartyRoleRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartyRoleRefRepository extends JpaRepository<PartyRoleRef, UUID> {
}
