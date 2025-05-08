package com.tahaakocer.crm.mapper;

import com.tahaakocer.crm.dto.RoleTypeRefDto;
import com.tahaakocer.crm.model.RoleTypeRef;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface RoleTypeRefMapper {

    RoleTypeRefDto entityToDto(RoleTypeRef saved);

    RoleTypeRef dtoToEntity(RoleTypeRefDto roleTypeRefDto);
}
