package com.tahaakocer.account.mapper;

import com.tahaakocer.account.model.ContactMediumCharacteristic;
import com.tahaakocer.commondto.order.AddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactMediumMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "createDate")
    @Mapping(ignore = true, target = "updateDate")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(ignore = true, target = "lastModifiedBy")
    @Mapping(ignore = true, target = "contactMedium")
    ContactMediumCharacteristic AddressDtoToContactMediumCharacteristicEntity(AddressDto addressDto);

}
