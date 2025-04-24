package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.ActiveStatusDefinedByDto;
import com.tahaakocer.orderservice.model.mongo.ActiveStatusDefinedBy;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ActiveStatusDefinedByMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateActiveStatusDefinedByFromDto(@MappingTarget ActiveStatusDefinedBy target, ActiveStatusDefinedByDto source);
}
