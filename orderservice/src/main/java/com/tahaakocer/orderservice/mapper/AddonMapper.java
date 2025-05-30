package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.commondto.order.AddonDto;
import com.tahaakocer.orderservice.model.Addon;
import lombok.experimental.SuperBuilder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SuperBuilder.class})
public interface AddonMapper {

    Addon addonDtoToAddon(AddonDto addonDto);

    AddonDto addonToAddonDto(Addon saved);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddonDto(@MappingTarget AddonDto target, AddonDto source);

    List<AddonDto> addonsToAddonDtos(List<Addon> addons);
}