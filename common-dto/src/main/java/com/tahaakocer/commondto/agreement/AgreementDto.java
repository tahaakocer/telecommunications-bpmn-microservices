package com.tahaakocer.commondto.agreement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
import com.tahaakocer.commondto.order.PartyRoleRefDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementDto extends BaseDto {
    private List<AgreementCharacteristicDto> agreementCharacteristics;
    private PartyRoleRefDto partyRoleRef;
    private List<AgreementItemDto> agreementItems;
    private LocalDateTime agreementPeriodEndDateTime;
    private LocalDateTime agreementPeriodStartDateTime;
    private String description;
    private String name;
    private String type;
    private Integer version;
}
