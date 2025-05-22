package com.tahaakocer.account.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "contact_medium_characteristic", schema = "account_management")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ContactMediumCharacteristic extends BaseEntity{
    @OneToOne(mappedBy = "contactMediumCharacteristic", cascade = CascadeType.ALL, orphanRemoval = true)
    private ContactMedium contactMedium;

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

    private String formattedAddress;
    private Long phoneNumber;
    private String email;
}
