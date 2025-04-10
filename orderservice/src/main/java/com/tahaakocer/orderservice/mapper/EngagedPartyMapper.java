package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.EngagedPartyDto;
import com.tahaakocer.orderservice.model.mongo.EngagedParty;
import lombok.experimental.SuperBuilder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = SuperBuilder.class)
public interface EngagedPartyMapper {


    EngagedParty dtoToEntity(EngagedPartyDto engagedParty);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEngagedPartyFromDto( @MappingTarget EngagedParty target,EngagedPartyDto source);

}
