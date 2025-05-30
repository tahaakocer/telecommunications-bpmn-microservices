package com.tahaakocer.agreement.service;


import com.tahaakocer.agreement.exception.GeneralException;
import com.tahaakocer.agreement.model.Agreement;
import com.tahaakocer.agreement.model.PartyRoleRef;
import com.tahaakocer.agreement.repository.PartyRoleRefRepository;
import com.tahaakocer.commondto.order.OrderRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PartyRoleRefService {
    private final PartyRoleRefRepository partyRoleRefRepository;

    public PartyRoleRefService(PartyRoleRefRepository partyRoleRefRepository) {
        this.partyRoleRefRepository = partyRoleRefRepository;
    }

    protected PartyRoleRef createPartyRoleRefByAgreement(OrderRequestDto orderRequestDto, Agreement agreement) {
        log.info("Creating PartyRoleRef for agreement ID: {} from order request ID: {}",
                agreement.getId(), orderRequestDto.getId());
        UUID refPartyRoleId = orderRequestDto.getBaseOrder().getPartyRoleRef().getRefPartyRoleId();
        if (refPartyRoleId == null) {
            log.error("PartyRoleRef ID is null for order request ID: {}", orderRequestDto.getId());
            throw new GeneralException("PartyRoleRef ID cannot be null");
        }
        PartyRoleRef partyRoleRef = new PartyRoleRef();
        partyRoleRef.setRefPartyRoleId(refPartyRoleId);
        partyRoleRef.setAgreement(agreement);
        log.info("PartyRoleRef created with ID: {}", partyRoleRef.getRefPartyRoleId());
        return this.save(partyRoleRef);
    }
    private PartyRoleRef save(PartyRoleRef partyRoleRef) {
        try{
            PartyRoleRef saved = this.partyRoleRefRepository.save(partyRoleRef);
            log.info("PartyRoleRef saved: " + saved);
            return saved;
        }catch (Exception e){
            log.error("PartyRoleRef save failed: " + e.getMessage());
            throw new GeneralException("PartyRoleRef save failed: " + e.getMessage());
        }
    }
}
