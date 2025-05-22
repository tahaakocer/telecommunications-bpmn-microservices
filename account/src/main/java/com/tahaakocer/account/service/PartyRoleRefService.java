package com.tahaakocer.account.service;

import com.tahaakocer.account.exception.GeneralException;
import com.tahaakocer.account.model.Account;
import com.tahaakocer.account.model.PartyRoleRef;
import com.tahaakocer.account.repository.PartyRoleRefRepository;
import com.tahaakocer.commondto.order.PartyRoleRefDto;
import jakarta.mail.Part;
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

    protected PartyRoleRef createPartyRoleRefByAccount(UUID refPartyRoleId, Account account) {
        PartyRoleRef partyRoleRef = new PartyRoleRef();
        partyRoleRef.setRefPartyRoleId(refPartyRoleId);
        partyRoleRef.setAccount(account);
        PartyRoleRef saved = this.save(partyRoleRef);
        return saved;
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
