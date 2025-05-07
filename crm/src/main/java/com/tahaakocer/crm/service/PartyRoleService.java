package com.tahaakocer.crm.service;

import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.model.PartyRole;
import com.tahaakocer.crm.repository.PartyRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PartyRoleService {
    private final PartyRoleRepository partyRoleRepository;

    public PartyRoleService(PartyRoleRepository partyRoleRepository) {
        this.partyRoleRepository = partyRoleRepository;
    }

    @Transactional
    public PartyRole createPartyRoleWithCustomer() {
        PartyRole saved;
        try {
            PartyRole partyRole = new PartyRole();
            partyRole.setRoleType("CUSTOMER");
            log.info("Creating empty party role");
            saved = partyRoleRepository.save(partyRole);
        } catch (Exception e) {
            log.error("Error occurred while creating empty party role: {}", e.getMessage());
            throw new GeneralException("Failed to create empty party role");
        }
        return saved;
    }
}