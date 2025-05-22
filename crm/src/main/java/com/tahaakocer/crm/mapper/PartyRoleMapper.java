package com.tahaakocer.crm.mapper;


import com.tahaakocer.commondto.crm.PartyRoleCharacteristicDto;
import com.tahaakocer.commondto.crm.PartyRoleDto;
import com.tahaakocer.crm.model.Characteristic;
import com.tahaakocer.crm.model.PartyRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PartyRoleMapper {

    @Mapping(target = "customer.partyRole", ignore = true)
    @Mapping(target = "partner.partyRole", ignore = true)
    @Mapping(target = "partner.partnerUser.partner", ignore = true)
    @Mapping(target = "characteristics", expression = "java(mapCharacteristics(partyRole.getCharacteristics()))")
    PartyRoleDto entityToDto(PartyRole partyRole);

    default List<PartyRoleCharacteristicDto> mapCharacteristics(List<Characteristic> characteristics) {
        if (characteristics == null) {
            return null;
        }
        return characteristics.stream()
                .map(characteristic -> PartyRoleCharacteristicDto.builder()
                        .name(characteristic.getName())
                        .value(characteristic.getValue())
                        .build())
                .collect(Collectors.toList());
    }

}