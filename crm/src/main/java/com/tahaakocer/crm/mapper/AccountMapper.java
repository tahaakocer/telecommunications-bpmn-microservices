package com.tahaakocer.crm.mapper;

import com.tahaakocer.crm.dto.AccountDto;
import com.tahaakocer.crm.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto entityToDto(Account saved);
}
