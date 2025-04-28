package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.commondto.order.AddressDto;
import com.tahaakocer.orderservice.model.mongo.Address;
import lombok.experimental.SuperBuilder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface AddressMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromDto(@MappingTarget Address target, AddressDto source);
}
