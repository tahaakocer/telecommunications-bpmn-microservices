package com.tahaakocer.crm.service;


import com.tahaakocer.commondto.crm.PartyRoleDto;
import com.tahaakocer.commondto.crm.RoleTypeRefDto;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.mapper.PartyRoleMapper;
import com.tahaakocer.crm.mapper.RoleTypeRefMapper;
import com.tahaakocer.crm.model.PartyRole;
import com.tahaakocer.crm.model.RoleTypeRef;
import com.tahaakocer.crm.repository.PartyRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class PartyRoleService {
    private final PartyRoleRepository partyRoleRepository;
    private final PartyRoleMapper partyRoleMapper;
    private final RoleTypeRefService roleTypeRefService;
    private final RoleTypeRefMapper roleTypeRefMapper;

    public PartyRoleService(PartyRoleRepository partyRoleRepository,
                            PartyRoleMapper partyRoleMapper,
                            RoleTypeRefService roleTypeRefService,
                            RoleTypeRefMapper roleTypeRefMapper) {
        this.partyRoleRepository = partyRoleRepository;
        this.partyRoleMapper = partyRoleMapper;
        this.roleTypeRefService = roleTypeRefService;
        this.roleTypeRefMapper = roleTypeRefMapper;
    }

    @Transactional
    public PartyRole createPartyRole(String roleType) {
        PartyRole saved;
        try {
            PartyRole partyRole = new PartyRole();

            RoleTypeRefDto roleTypeRefDto = this.roleTypeRefService.getRoleTypeRef(roleType);
            RoleTypeRef roleTypeRef = this.roleTypeRefMapper.dtoToEntity(roleTypeRefDto);
            partyRole.setRoleTypeRef(roleTypeRef);
            log.info("Creating empty party role");
            saved = partyRoleRepository.save(partyRole);
        } catch (Exception e) {
            log.error("Error occurred while creating empty party role: {}", e.getMessage());
            throw new GeneralException("Failed to create empty party role");
        }
        return saved;
    }

    private PartyRole save(PartyRole partyRole) {
        try{
            PartyRole saved = this.partyRoleRepository.save(partyRole);
            log.info("PartyRole saved: " + saved);
            return saved;
        }catch (Exception e){
            log.error("PartyRole save failed: " + e.getMessage());
            throw new GeneralException("PartyRole save failed: " + e.getMessage());
        }
    }
    public PartyRoleDto getPartyRole(String partyRoleId) {
        PartyRole partyRole = partyRoleRepository.findById(UUID.fromString(partyRoleId))
                .orElseThrow(() -> new GeneralException("Party role not found"));
        PartyRoleDto partyRoleDto = partyRoleMapper.entityToDto(partyRole);
        log.info("Party role: {}", partyRoleDto);
        return partyRoleDto;
    }
    public PartyRoleDto getPartyRoleByOrderRequestId(String orderRequestId) {
        PartyRole partyRole = this.getPartyRoleEntityByOrderRequestId(orderRequestId);
        PartyRoleDto partyRoleDto = partyRoleMapper.entityToDto(partyRole);
        log.info("Party role: {}", partyRoleDto);
        return partyRoleDto;
    }
    protected PartyRole getPartyRoleEntityByOrderRequestId(String orderRequestId) {
        PartyRole partyRole = partyRoleRepository.findByOrderRequestIdCharacteristic(orderRequestId)
                .orElseThrow(() -> new GeneralException("Party role not found"));
        log.info("Party role: {}", partyRole.getId());
        return partyRole;
    }

}