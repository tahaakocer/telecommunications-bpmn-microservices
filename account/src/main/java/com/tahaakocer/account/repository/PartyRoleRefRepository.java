package com.tahaakocer.account.repository;

import com.tahaakocer.account.model.PartyRoleRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartyRoleRefRepository extends JpaRepository<PartyRoleRef, UUID> {
}
