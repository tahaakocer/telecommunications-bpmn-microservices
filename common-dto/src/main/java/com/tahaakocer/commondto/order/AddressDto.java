package com.tahaakocer.commondto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto extends BaseDto {
    private Integer bbk;

    private Integer cityCode;
    private Integer districtCode;
    private Integer townshipCode;
    private Integer villageCode;
    private Integer neighborhoodCode;
    private Integer streetCode;
    private Integer buildingCode;
    private Integer flat;



    private String cityName;
    private String districtName;
    private String townshipName;
    private String villageName;
    private String neighborhoodName;
    private String streetName;
    private String outsideDoorCode;
    private String blokName;
    private String siteName;
    private Integer flatNo;

}
