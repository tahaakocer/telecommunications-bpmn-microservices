package com.tahaakocer.externalapiservice.dto.bbk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TTAddressDto {
    @JsonProperty("UlkeKodu")
    private Object countryCode;

    @JsonProperty("UlkeAdi")
    private Object countryName;

    @JsonProperty("IlKodu")
    private Object cityCode;

    @JsonProperty("IlAdi")
    private Object cityName;

    @JsonProperty("IlceKodu")
    private Object districtCode;

    @JsonProperty("IlceAdi")
    private Object districtName;

    @JsonProperty("BucakKodu")
    private Object townshipCode;

    @JsonProperty("BucakAdi")
    private Object townshipName;

    @JsonProperty("KoyKodu")
    private Object villageCode;

    @JsonProperty("KoyAdi")
    private Object villageName;

    @JsonProperty("MahalleKodu")
    private Object neighborhoodCode;

    @JsonProperty("MahalleAdi")
    private Object neighborhoodName;

    @JsonProperty("CSBMKodu")
    private Object streetCode;

    @JsonProperty("CSBMAdi")
    private Object streetName;

    @JsonProperty("BinaKodu")
    private Object buildingCode;

    @JsonProperty("DisKapiNo")
    private Object outsideDoorCode;

    @JsonProperty("BlokAdi")
    private Object blokName;

    @JsonProperty("SiteAdi")
    private Object siteName;

    @JsonProperty("BBolumKodu")
    private Object bbk;

    @JsonProperty("IcKapiNo")
    private Object flatNo;

    @JsonProperty("PostaKodu")
    private Object postalCode;

    @JsonProperty("AcikAdres")
    private Object fullAddress;
}
