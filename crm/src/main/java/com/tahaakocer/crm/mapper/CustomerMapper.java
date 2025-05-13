package com.tahaakocer.crm.mapper;

import com.tahaakocer.crm.dto.CustomerDto;
import com.tahaakocer.crm.model.Customer;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {SuperBuilder.class, CharacteristicMapper.class})
public interface CustomerMapper {
    @Mapping(target = "partyRole.characteristics.partyRole", ignore = true)
    @Mapping(target = "partyRole.customer", ignore = true)
    CustomerDto entityToDto(Customer saved);

}
