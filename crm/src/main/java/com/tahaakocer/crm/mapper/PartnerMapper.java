package com.tahaakocer.crm.mapper;

import com.tahaakocer.crm.dto.PartnerDto;
import com.tahaakocer.crm.dto.PartnerRegisterRequest;
import com.tahaakocer.crm.dto.PartnerRegisterResponse;
import com.tahaakocer.crm.model.Partner;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface PartnerMapper {

    @Mapping(target= "partyRole.partner",ignore = true)
    @Mapping(target = "partnerUser.partner", ignore = true)
    PartnerDto entityToDto(Partner savedPartner);
}
