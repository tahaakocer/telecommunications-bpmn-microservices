package com.tahaakocer.externalapiservice.mapper;

import com.tahaakocer.externalapiservice.dto.bbk.FullAddressResponse;
import com.tahaakocer.externalapiservice.dto.bbk.TTAddressDto;
import com.tahaakocer.externalapiservice.dto.orderRequestDto.AddressDto;
import org.mapstruct.*;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "countryCode", target = "countryCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "countryName", target = "countryName", qualifiedByName = "objectToString")
    @Mapping(source = "cityCode", target = "cityCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "cityName", target = "cityName", qualifiedByName = "objectToString")
    @Mapping(source = "districtCode", target = "districtCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "districtName", target = "districtName", qualifiedByName = "objectToString")
    @Mapping(source = "townshipCode", target = "townshipCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "townshipName", target = "townshipName", qualifiedByName = "objectToString")
    @Mapping(source = "villageCode", target = "villageCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "villageName", target = "villageName", qualifiedByName = "objectToString")
    @Mapping(source = "neighborhoodCode", target = "neighborhoodCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "neighborhoodName", target = "neighborhoodName", qualifiedByName = "objectToString")
    @Mapping(source = "streetCode", target = "streetCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "streetName", target = "streetName", qualifiedByName = "objectToString")
    @Mapping(source = "buildingCode", target = "buildingCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "outsideDoorCode", target = "outsideDoorCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "blokName", target = "blokName", qualifiedByName = "objectToString")
    @Mapping(source = "siteName", target = "siteName", qualifiedByName = "objectToString")
    @Mapping(source = "bbk", target = "bbk", qualifiedByName = "objectToInteger")
    @Mapping(source = "flatNo", target = "flatNo", qualifiedByName = "objectToInteger")
    @Mapping(source = "postalCode", target = "postalCode", qualifiedByName = "objectToInteger")
    @Mapping(source = "fullAddress", target = "fullAddress", qualifiedByName = "objectToString")
    FullAddressResponse ttAddressDtoToFullAddressResponse(TTAddressDto address);


    AddressDto fullAddressResponseToAddressDto(FullAddressResponse address);
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateAddressDtoFromFullAddressResponse(@MappingTarget AddressDto target,FullAddressResponse source);
    @AfterMapping
    default void processEmptyBrackets(Object source, @MappingTarget Object target) {
        try {
            // Boş köşeli parantez için regex pattern (herhangi sayıda boşluk içerebilir)
            Pattern emptyBracketsPattern = Pattern.compile("^\\[\\s*\\]$");

            Field[] fields = target.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == String.class) {
                    Object value = field.get(target);
                    if (value instanceof String) {
                        String strValue = (String) value;
                        if (strValue != null && emptyBracketsPattern.matcher(strValue.trim()).matches()) {
                            field.set(target, null);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            // Hata durumunu loglama veya exception fırlatma yapabilirsiniz
        }
    }
    @Named("objectToInteger")
    default Integer objectToInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        try {
            return Integer.valueOf(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Named("objectToString")
    default String objectToString(Object obj) {
        return obj == null ? null : obj.toString();
    }
}