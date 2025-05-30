package com.tahaakocer.agreement.mapper;

import com.tahaakocer.agreement.model.Agreement;
import com.tahaakocer.agreement.model.AgreementCharacteristic;
import com.tahaakocer.commondto.agreement.AgreementCharacteristicDto;
import com.tahaakocer.commondto.agreement.AgreementDto;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        AgreementItemMapper.class,
        SuperBuilder.class
    }
)
public interface AgreementMapper {
    @Named("mapAgreementCharacteristicsEntityToDto")
    default List<AgreementCharacteristicDto> mapCharacteristicsEntityToDto(List<AgreementCharacteristic> characteristics) {
        if (characteristics == null) {
            return null;
        }
        return characteristics.stream()
                .map(characteristic -> {
                    AgreementCharacteristicDto dto = new AgreementCharacteristicDto();
                    dto.setAgreement(null);
                    dto.setName(characteristic.getName());
                    dto.setValue(characteristic.getValue());
                    return dto;
                })
                .toList();
    }
    @Mapping(target = "agreementCharacteristics", qualifiedByName = "mapAgreementCharacteristicsEntityToDto")
    AgreementDto entityToDto(Agreement saved);
}
