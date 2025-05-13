package com.tahaakocer.crm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "contact_medium_characteristic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactMediumCharacteristic extends BaseEntity {

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