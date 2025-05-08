package com.tahaakocer.crm.service;

import com.tahaakocer.commondto.order.EngagedPartyDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.model.Individual;
import com.tahaakocer.crm.model.IndividualIdentification;
import com.tahaakocer.crm.model.PartnerUser;
import com.tahaakocer.crm.model.PartyRole;
import com.tahaakocer.crm.repository.IndividualRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IndividualService {
    private final IndividualRepository individualRepository;

    public IndividualService(IndividualRepository individualRepository) {
        this.individualRepository = individualRepository;
    }

    @Transactional
    protected Individual createIndividualWithOrderRequest(OrderRequestDto orderRequestDto, PartyRole partyRole) {
        EngagedPartyDto engagedPartyDto = orderRequestDto.getBaseOrder().getEngagedParty();

        IndividualIdentification individualIdentification = new IndividualIdentification();
        individualIdentification.setIdentificationId(engagedPartyDto.getTckn());

        Individual individual = new Individual();
        individual.setIndividualIdentification(individualIdentification);
        individual.setFirstName(engagedPartyDto.getFirstName());
        individual.setLastName(engagedPartyDto.getLastName());
        individual.setBirthYear(engagedPartyDto.getBirthYear());
        individual.setFormattedName(getFormattedName(engagedPartyDto.getFirstName(), engagedPartyDto.getLastName()));
        individual.setPartyRole(partyRole);
        individualIdentification.setIndividual(individual);
        return this.saveIndividual(individual);
    }
    protected Individual createIndividualWithPartnerUser(PartnerUser partnerUser, PartyRole partyRole) {
        IndividualIdentification individualIdentification = new IndividualIdentification();
        individualIdentification.setIdentificationId(partnerUser.getTckn());
        Individual individual = new Individual();
        individual.setIndividualIdentification(individualIdentification);
        individual.setFirstName(partnerUser.getFirstName());
        individual.setLastName(partnerUser.getLastName());
        individual.setBirthYear(partnerUser.getBirthYear());
        individual.setFormattedName(getFormattedName(partnerUser.getFirstName(), partnerUser.getLastName()));
        individual.setPartyRole(partyRole);
        individualIdentification.setIndividual(individual);
        return this.saveIndividual(individual);

    }
    private String getFormattedName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
    public Individual saveIndividual(Individual individual) {
        try {
            Individual saved =  individualRepository.save(individual);
            log.info("Saving individual " + individual.getIndividualIdentification().getIdentificationId());
            return saved;
        } catch (Exception e) {
            log.error("Error occurred while saving individual: {}", e.getMessage());
            throw new GeneralException("Failed to save individual");
        }
    }
}
