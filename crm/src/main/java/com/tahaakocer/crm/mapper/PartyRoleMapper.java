package com.tahaakocer.crm.mapper;

import com.tahaakocer.crm.dto.PartyRoleDto;
import com.tahaakocer.crm.model.PartyRole;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface PartyRoleMapper {
    @Mapping(target = "customer.partyRole", ignore = true)
    @Mapping(target = "partner.partyRole", ignore = true)
    @Mapping(target = "partner.partnerUser.partner", ignore = true)
    PartyRoleDto entityToDto(PartyRole partyRole);
//    PartyRole dtoToEntity(PartyRoleDto partyRoleDto);
}
