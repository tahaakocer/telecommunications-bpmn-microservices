package com.tahaakocer.crm.mapper;


import com.tahaakocer.commondto.crm.PartyRoleCharacteristicDto;
import com.tahaakocer.crm.model.Characteristic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CharacteristicMapper {

    @Mapping(target = "partyRole", ignore = true)
    PartyRoleCharacteristicDto entityToDto(Characteristic characteristic);

    default List<PartyRoleCharacteristicDto> mapCharacteristics(List<Characteristic> characteristics) {
        if (characteristics == null) {
            return null;
        }

        return characteristics.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}