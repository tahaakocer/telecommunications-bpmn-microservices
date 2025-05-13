package com.tahaakocer.crm.mapper;

import com.tahaakocer.crm.dto.CharacteristicDto;
import com.tahaakocer.crm.dto.PartyRoleDto;
import com.tahaakocer.crm.model.Characteristic;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CharacteristicMapper {

    @Mapping(target = "partyRole", ignore = true)
    CharacteristicDto entityToDto(Characteristic characteristic);

    default List<CharacteristicDto> mapCharacteristics(List<Characteristic> characteristics) {
        if (characteristics == null) {
            return null;
        }

        return characteristics.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}