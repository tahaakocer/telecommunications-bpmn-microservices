package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.AddonDto;
import com.tahaakocer.orderservice.model.mongo.Addon;
import lombok.experimental.SuperBuilder;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SuperBuilder.class})
public interface AddonMapper {

    Addon addonDtoToAddon(AddonDto addonDto);

    AddonDto addonToAddonDto(Addon saved);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddonDto(@MappingTarget AddonDto target, AddonDto source);

    List<AddonDto> addonsToAddonDtos(List<Addon> addons);
}