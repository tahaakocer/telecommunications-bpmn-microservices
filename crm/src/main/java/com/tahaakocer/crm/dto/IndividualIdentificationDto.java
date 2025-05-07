package com.tahaakocer.crm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.order.BaseDto;
import com.tahaakocer.crm.model.Individual;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndividualIdentificationDto extends BaseDto {
//    private Individual individual;

    private Long identificationId;

}
