package com.tahaakocer.commondto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.commondto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder

public class BaseOrderDto extends BaseDto {

    private String code;
    private EngagedPartyDto engagedParty;
    private AccountRefDto accountRef;
    private BpmnFlowRefDto bpmnFlowRef;
    private List<OrderItemDto> orderItems;
    private List<CharacteristicDto> characteristics;
    private PartyRoleRefDto partyRoleRef;
    private AgreementRefDto agreementRef;
    private String orderType;
    private Boolean isDraft;

}
