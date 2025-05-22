package com.tahaakocer.crm.mapper;

import com.tahaakocer.commondto.crm.AccountRefDto;
import com.tahaakocer.crm.model.AccountRef;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountRefMapper {

    AccountRefDto entityToDto(AccountRef accountRef);
}
