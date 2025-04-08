package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.BpmnFlowRefDto;
import com.tahaakocer.orderservice.model.mongo.BpmnFlowRef;
import lombok.experimental.SuperBuilder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = SuperBuilder.class)
public interface BpmnFlowRefMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBpmnFlowRefFromDto(@MappingTarget BpmnFlowRef target, BpmnFlowRefDto source);
}
