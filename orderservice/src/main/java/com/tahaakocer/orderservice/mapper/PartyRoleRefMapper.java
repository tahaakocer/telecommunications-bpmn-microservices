package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.commondto.order.PartyRoleRefDto;
import com.tahaakocer.orderservice.model.mongo.PartyRoleRef;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PartyRoleRefMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartyRoleRefFromDto(@MappingTarget PartyRoleRef target, PartyRoleRefDto source);
}
