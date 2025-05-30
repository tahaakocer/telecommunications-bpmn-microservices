package com.tahaakocer.agreement.mapper;

import com.tahaakocer.agreement.model.AgreementItem;
import com.tahaakocer.commondto.agreement.AgreementItemDto;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface AgreementItemMapper {

    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "product.agreementItem",ignore = true)
    @Mapping(target = "agreementItemStatus.agreementItem", ignore = true)
    AgreementItemDto entityToDto(AgreementItem agreementItem);
}
