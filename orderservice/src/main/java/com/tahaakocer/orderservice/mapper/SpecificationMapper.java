package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.SpecificationDto;
import com.tahaakocer.orderservice.model.mongo.Specification;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface SpecificationMapper {

    Specification dtoToEntity(SpecificationDto specificationDto);
    SpecificationDto entityToDto(Specification saved);
    List<SpecificationDto> entityToDtoList(List<Specification> specifications);
}
