package com.tahaakocer.crm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.order.BaseDto;
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
public class AccountDto extends BaseDto {
    private String accountCode;

    private String accountName;
    private LocalDateTime endDate;

    private String formattedBillingAddress;

}
