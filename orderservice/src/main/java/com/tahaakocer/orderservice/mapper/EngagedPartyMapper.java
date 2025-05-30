package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.commondto.order.EngagedPartyDto;
import com.tahaakocer.orderservice.model.EngagedParty;
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
