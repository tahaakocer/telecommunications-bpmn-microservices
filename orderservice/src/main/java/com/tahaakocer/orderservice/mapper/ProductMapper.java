package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.ProductDto;
import com.tahaakocer.orderservice.model.mongo.Product;
import lombok.experimental.SuperBuilder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {CharacteristicMapper.class, SuperBuilder.class})
public interface ProductMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(@MappingTarget Product target, ProductDto source);
}
