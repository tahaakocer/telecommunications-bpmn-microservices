package com.tahaakocer.crm.mapper;

import com.tahaakocer.commondto.order.AddressDto;
import com.tahaakocer.crm.model.ContactMediumCharacteristic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactMediumMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "createdAt")
    @Mapping(ignore = true, target = "updatedAt")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(ignore = true, target = "updatedBy")
    ContactMediumCharacteristic AddressDtoToContactMediumCharacteristicEntity(AddressDto addressDto);

}
