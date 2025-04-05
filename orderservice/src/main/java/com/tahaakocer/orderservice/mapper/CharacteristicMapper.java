package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.request.CharacteristicCreateRequest;
import com.tahaakocer.orderservice.dto.CharacteristicDto;
import com.tahaakocer.orderservice.dto.response.CharacteristicGetAllResponse;
import com.tahaakocer.orderservice.model.mongo.BaseModel;
import com.tahaakocer.orderservice.model.mongo.Characteristic;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = {SuperBuilder.class})
public interface CharacteristicMapper {

    Characteristic dtoToCharacteristic(CharacteristicDto characteristicDto);

    CharacteristicDto characteristicToDto(Characteristic saved);

    CharacteristicDto characteristicCreateRequestToDto(CharacteristicCreateRequest characteristicCreateRequest);

    List<CharacteristicDto> characteristicsToDtos(List<Characteristic> characteristics);

    List<CharacteristicGetAllResponse> characteristicDtoListToGetAllResponseList(List<CharacteristicDto> characteristics);
}
