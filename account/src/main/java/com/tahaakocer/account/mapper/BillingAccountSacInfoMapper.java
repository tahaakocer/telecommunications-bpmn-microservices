package com.tahaakocer.account.mapper;

import com.tahaakocer.account.model.BillingAccountSacInfo;
import com.tahaakocer.commondto.order.SacInfoDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BillingAccountSacInfoMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "createDate")
    @Mapping(ignore = true, target = "updateDate")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(ignore = true, target = "lastModifiedBy")
    @Mapping(ignore = true, target = "account")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void sacInfoDtoToBillingAccountSacInfoEntity(@MappingTarget BillingAccountSacInfo billingAccountSacInfo, SacInfoDto sacInfoDto);
}
