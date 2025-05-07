package com.tahaakocer.crm.mapper;

import com.tahaakocer.crm.dto.PartnerDto;
import com.tahaakocer.crm.dto.PartnerRegisterRequest;
import com.tahaakocer.crm.model.Partner;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface PartnerMapper {
    PartnerDto requestToDto(PartnerRegisterRequest partnerRegisterRequest);

    Partner dtoToEntity(PartnerDto partnerDto);
}
