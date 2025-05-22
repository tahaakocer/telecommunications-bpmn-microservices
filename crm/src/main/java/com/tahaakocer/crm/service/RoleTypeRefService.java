package com.tahaakocer.crm.service;

import com.tahaakocer.commondto.crm.RoleTypeRefDto;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.mapper.RoleTypeRefMapper;
import com.tahaakocer.crm.model.RoleTypeRef;
import com.tahaakocer.crm.repository.RoleTypeRefRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleTypeRefService {
    private final RoleTypeRefRepository roleTypeRefRepository;
    private final RoleTypeRefMapper roleTypeRefMapper;

    public RoleTypeRefService(RoleTypeRefRepository roleTypeRefRepository,
                              RoleTypeRefMapper roleTypeRefMapper) {
        this.roleTypeRefRepository = roleTypeRefRepository;
        this.roleTypeRefMapper = roleTypeRefMapper;
    }

    public RoleTypeRefDto createRoleTypeRef(String name) {
        boolean isExist = this.roleTypeRefRepository.existsByName(name);
        if(isExist) {
            log.error("RoleTypeRef with name {} already exists", name);
            throw new GeneralException("RoleTypeRef with this name already exists");
        }
        RoleTypeRef roleTypeRef = new RoleTypeRef();
        roleTypeRef.setName(name);
        RoleTypeRef saved =  roleTypeRefRepository.save(roleTypeRef);
        log.info("RoleTypeRef with name {} has been created", name);
        return roleTypeRefMapper.entityToDto(saved);
    }
    public RoleTypeRefDto getRoleTypeRef(String name) {
        RoleTypeRef roleTypeRef = this.roleTypeRefRepository.findByName(name)
                .orElseThrow(() -> new GeneralException("RoleTypeRef not found"));
        log.info("RoleTypeRef with name {} has been retrieved", name);
        return roleTypeRefMapper.entityToDto(roleTypeRef);
    }
    public List<RoleTypeRefDto> getAllRoleTypeRefs() {
        List<RoleTypeRef> roleTypeRefs = this.roleTypeRefRepository.findAll();
        log.info("All RoleTypeRefs have been retrieved");
        return roleTypeRefs.stream().map(roleTypeRefMapper::entityToDto).toList();
    }
}
