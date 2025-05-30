package com.tahaakocer.agreement.service;

import com.tahaakocer.agreement.client.OrderRequestServiceClient;
import com.tahaakocer.agreement.exception.GeneralException;
import com.tahaakocer.agreement.mapper.AgreementMapper;
import com.tahaakocer.agreement.model.Agreement;
import com.tahaakocer.agreement.model.AgreementItem;
import com.tahaakocer.agreement.model.PartyRoleRef;
import com.tahaakocer.agreement.repository.AgreementRepository;
import com.tahaakocer.commondto.agreement.AgreementDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AgreementService {
    private final AgreementRepository agreementRepository;
    private final PartyRoleRefService partyRoleRefService;
    private final AgreementMapper agreementMapper;
    private final AgreementItemService agreementItemService;

    private final OrderRequestServiceClient orderRequestServiceClient;
    public AgreementService(AgreementRepository agreementRepository,
                            PartyRoleRefService partyRoleRefService,
                            AgreementMapper agreementMapper,
                            AgreementItemService agreementItemService,
                            OrderRequestServiceClient orderRequestServiceClient) {
        this.agreementRepository = agreementRepository;
        this.partyRoleRefService = partyRoleRefService;
        this.agreementMapper = agreementMapper;
        this.agreementItemService = agreementItemService;
        this.orderRequestServiceClient = orderRequestServiceClient;
    }

    public AgreementDto createAgreement(String orderRequestId) {
        log.info("Creating agreement for order request ID: {}", orderRequestId);
        OrderRequestDto orderRequestDto = this.callGetOrderRequestMethod(orderRequestId);
        Agreement agreement = new Agreement();
        agreement.setAgreementCharacteristics(new ArrayList<>());
//        agreement.setAgreementItems(new ArrayList<>());
        agreement.setType("STANDARD");
        Agreement savedAgreement = this.saveAgreementEntity(agreement);
        List<AgreementItem> agreementItems = agreementItemService.createAgreementItemEntitiesWithOrder(orderRequestDto, savedAgreement);

        PartyRoleRef partyRoleRef = this.partyRoleRefService.createPartyRoleRefByAgreement(orderRequestDto,savedAgreement);
        savedAgreement.setPartyRoleRef(partyRoleRef);
        savedAgreement.setAgreementItems(agreementItems);
        return this.agreementMapper.entityToDto(savedAgreement);
    }
    private OrderRequestDto callGetOrderRequestMethod(String orderRequestId) {
        try {
            var response = orderRequestServiceClient.getOrderRequest(UUID.fromString(orderRequestId));
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getData();
            } else {
                log.error("Failed to retrieve order request with ID: {}", orderRequestId);
                throw new GeneralException("Failed to retrieve order request");
            }
        } catch (Exception e) {
            log.error("Error calling getOrderRequest method: {}", e.getMessage());
            throw new GeneralException("Error calling getOrderRequest method", e);
        }
    }
    private Agreement saveAgreementEntity(Agreement agreement) {
        try{
            Agreement savedAgreement = agreementRepository.save(agreement);
            log.info("Agreement saved successfully with ID: {}", savedAgreement.getId());
            return savedAgreement;
        } catch (Exception e) {
            log.error("Error saving agreement: {}", e.getMessage());
            throw new GeneralException("Failed to save agreement", e);
        }
    }
}
