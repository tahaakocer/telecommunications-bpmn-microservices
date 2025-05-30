package com.tahaakocer.commondto.agreement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementItemDto extends BaseDto {
    private AgreementDto agreement;
    private ProductDto product;
    private AgreementItemStatusDto agreementItemStatus;

    private AgreementItemAccountRefDto agreementItemAccountRef;
    private LocalDateTime endDateTime;
    private LocalDateTime startDateTime;
    private Integer itemNumber;
}
