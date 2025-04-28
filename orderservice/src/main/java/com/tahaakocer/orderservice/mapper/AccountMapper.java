package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.commondto.order.AccountDto;
import com.tahaakocer.orderservice.model.mongo.Account;
import lombok.experimental.SuperBuilder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",uses = {SuperBuilder.class})
public interface AccountMapper {
    Account dtoToEntity(AccountDto accountDto);

    AccountDto entityToDto(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(@MappingTarget Account target, AccountDto source);
}
