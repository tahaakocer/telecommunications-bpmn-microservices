package com.tahaakocer.crm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.order.BaseDto;
import com.tahaakocer.commondto.order.CharacteristicDto;
import com.tahaakocer.crm.model.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyRoleDto extends BaseDto {

    private List<AccountRefDto> accountRefs;


    private List<AgreementRefDto> agreementRefs;

    private List<ContactMediumDto> contactMedia;

    private CustomerDto customer;

    private IndividualDto individual;

    private List<CharacteristicDto> characteristics = new ArrayList<>();
}
