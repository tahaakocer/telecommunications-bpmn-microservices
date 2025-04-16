package com.tahaakocer.externalapiservice.dto.bbk;

import lombok.Data;

@Data
public class FullAddressResponse {

    private Integer countryCode;
    private String countryName;
    private Integer cityCode;
    private String cityName;
    private Integer districtCode;
    private String districtName;
    private Integer townshipCode;
    private String townshipName;
    private Integer villageCode;
    private String villageName;
    private Integer neighborhoodCode;
    private String neighborhoodName;
    private Integer streetCode;
    private String streetName;
    private Integer buildingCode;
    private Integer outsideDoorCode;
    private String blokName;
    private String siteName;
    private Integer bbk;
    private Integer flatNo;
    private Integer postalCode;
    private String fullAddress;
}
