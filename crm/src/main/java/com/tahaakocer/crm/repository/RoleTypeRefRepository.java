package com.tahaakocer.crm.repository;


import com.tahaakocer.crm.model.RoleTypeRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleTypeRefRepository extends JpaRepository<RoleTypeRef, UUID> {
    Optional<RoleTypeRef> findByName(String name);

    boolean existsByName(String name);
}
