package com.tahaakocer.agreement.service;

import com.tahaakocer.agreement.client.OrderRequestServiceClient;
import com.tahaakocer.agreement.exception.GeneralException;
import com.tahaakocer.agreement.mapper.AgreementMapper;
import com.tahaakocer.agreement.model.Agreement;
import com.tahaakocer.agreement.model.AgreementItem;
import com.tahaakocer.agreement.model.PartyRoleRef;
import com.tahaakocer.agreement.repository.AgreementRepository;
import com.tahaakocer.commondto.agreement.AgreementDto;
import com.tahaakocer.commondto.order.AgreementRefDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
        createAgreementRefForOrderRequest(orderRequestDto.getId().toString(), savedAgreement);
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
    private OrderRequestResponse createAgreementRefForOrderRequest(String orderRequestId,Agreement agreement) {
        log.info("Creating agreement ref for order request ID: {}", orderRequestId);
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        AgreementRefDto agreementRefDto = new AgreementRefDto();
        agreementRefDto.setRefAgreementId(agreement.getId());
        orderUpdateDto.setAgreementRef(agreementRefDto);
        return this.callUpdateOrderRequestMethod(orderRequestId, orderUpdateDto);
    }
    private OrderRequestResponse callUpdateOrderRequestMethod(String orderRequestId, OrderUpdateDto orderUpdateDto) {
        try {
            ResponseEntity<GeneralResponse<OrderRequestResponse>> orderRequest = this.orderRequestServiceClient.updateOrderRequest(
                    UUID.fromString(orderRequestId), orderUpdateDto);
            GeneralResponse<OrderRequestResponse> body = orderRequest.getBody();

            if (body == null || body.getCode() != 200) {
                log.error("Failed to get orderRequest from order service client");
                throw new GeneralException("Failed to get orderRequest from order service client");
            }
            return body.getData();
        } catch (Exception e) {
            log.error("Error occurred while creating customer: {}", e.getMessage());
            throw new GeneralException("Failed to get orderRequest from order service client");
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


    public AgreementDto getAgreement(String agreementId) {
        log.info("Retrieving agreement with ID: {}", agreementId);
        Agreement agreement = agreementRepository.findById(UUID.fromString(agreementId))
                .orElseThrow(() -> new GeneralException("Agreement not found"));
        AgreementDto agreementDto = agreementMapper.entityToDto(agreement);
        log.info("Retrieved agreement: {}", agreementDto);
        return agreementDto;
    }
    public AgreementDto getAgreementByOrderId(String orderId) {
        log.info("Retrieving agreement with ID: {}", orderId);
        OrderRequestDto orderRequestDto = this.callGetOrderRequestMethod(orderId);
        if (orderRequestDto.getBaseOrder().getAgreementRef() == null || orderRequestDto.getBaseOrder().getAgreementRef().getRefAgreementId() == null) {
            log.error("No agreement found for order request ID: {}", orderId);
            throw new GeneralException("No agreement found for this order request");
        }
        AgreementDto agreementDto = this.getAgreement(
                orderRequestDto.getBaseOrder().getAgreementRef().getRefAgreementId().toString()
        );

        log.info("Retrieved agreement: {}", agreementDto);
        return agreementDto;
    }

}
