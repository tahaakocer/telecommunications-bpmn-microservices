package com.tahaakocer.camunda.mapper;

import com.tahaakocer.camunda.dto.ProcessDto;
import com.tahaakocer.camunda.model.ProcessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProcessMapper {
    ProcessDto entityToDto(ProcessEntity process);
    ProcessEntity dtoToEntity (ProcessDto processDto);
}
