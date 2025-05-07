package com.tahaakocer.crm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.crm.model.PartyRole;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountRefDto {
//    private PartyRole partyRole;

    private String accountCode;

    private UUID refAccountId;

    private LocalDateTime endDate;

}
