package com.tahaakocer.account.mapper;

import com.tahaakocer.account.model.Account;
import com.tahaakocer.account.model.Characteristic;
import com.tahaakocer.commondto.crm.AccountCharacteristicDto;
import com.tahaakocer.commondto.crm.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(ignore = true, target = "billingAccountSacInfo.account")
    @Mapping(target = "characteristics", qualifiedByName = "mapCharacteristics")
    @Mapping(ignore = true, target = "billingAccount.account")
    @Mapping(ignore = true, target = "billingAccount.billingSystem.billingAccount")
    AccountDto entityToDto(Account account);

    @Named("mapCharacteristics")
    default List<AccountCharacteristicDto> mapCharacteristics(List<Characteristic> characteristics) {
        if (characteristics == null) {
            return null;
        }
        return characteristics.stream()
                .map(characteristic -> {
                    AccountCharacteristicDto dto = new AccountCharacteristicDto();
                    dto.setAccount(null);
                    dto.setName(characteristic.getName());
                    dto.setValue(characteristic.getValue());
                    return dto;
                })
                .toList();
    }
}