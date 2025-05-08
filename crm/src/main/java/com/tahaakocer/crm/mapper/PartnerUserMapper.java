package com.tahaakocer.crm.mapper;

import com.tahaakocer.crm.dto.PartnerRegisterRequest;
import com.tahaakocer.crm.model.PartnerUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartnerUserMapper {

    PartnerUser partnerRegisterRequestToPartnerUserEntity(PartnerRegisterRequest partnerUserRegisterRequest);
}
