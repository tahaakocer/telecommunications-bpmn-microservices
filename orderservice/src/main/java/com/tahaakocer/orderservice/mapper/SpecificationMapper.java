package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.commondto.order.SpecificationDto;
import com.tahaakocer.orderservice.model.Specification;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface SpecificationMapper {

    Specification dtoToEntity(SpecificationDto specificationDto);
    SpecificationDto entityToDto(Specification saved);
    List<SpecificationDto> entityToDtoList(List<Specification> specifications);

    List<Specification> dtoToEntityList(List<SpecificationDto> specificationDtos);


}
