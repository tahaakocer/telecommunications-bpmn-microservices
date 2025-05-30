package com.tahaakocer.crm.mapper;

import com.tahaakocer.commondto.crm.PartnerDto;
import com.tahaakocer.crm.model.Partner;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {SuperBuilder.class, PartyRoleMapper.class})
public interface PartnerMapper {

    @Mapping(target= "partyRole.partner",ignore = true)
    @Mapping(target = "partnerUser.partner", ignore = true)
    PartnerDto entityToDto(Partner savedPartner);
}
