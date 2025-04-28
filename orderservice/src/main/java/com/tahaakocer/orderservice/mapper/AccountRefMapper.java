package com.tahaakocer.orderservice.mapper;


import com.tahaakocer.commondto.order.AccountRefDto;
import com.tahaakocer.orderservice.model.mongo.AccountRef;
import lombok.experimental.SuperBuilder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = SuperBuilder.class)
public interface AccountRefMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountRefFromDto(@MappingTarget AccountRef target, AccountRefDto source);
}
