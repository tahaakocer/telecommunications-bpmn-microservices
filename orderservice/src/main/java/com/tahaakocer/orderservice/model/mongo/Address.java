package com.tahaakocer.orderservice.model.mongo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Address extends BaseModel {
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
    private Integer outsideDoorCode;
    private String blokName;
    private String siteName;
    private Integer interiorDoorNo;

    private SacInfo sacInfo;
}
