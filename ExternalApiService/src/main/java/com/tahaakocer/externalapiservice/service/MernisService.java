package com.tahaakocer.externalapiservice.service;

import com.tahaakocer.externalapiservice.client.OrderRequestServiceClient;
import com.tahaakocer.externalapiservice.dto.GeneralResponse;
import com.tahaakocer.externalapiservice.dto.mernis.ValidMernisResponse;
import com.tahaakocer.externalapiservice.dto.orderRequestDto.EngagedPartyDto;
import com.tahaakocer.externalapiservice.dto.orderRequestDto.OrderRequestDto;
import com.tahaakocer.externalapiservice.exception.GeneralException;
import com.tahaakocer.externalapiservice.soap.NSNKPSPublicSoap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class MernisService {
    private final NSNKPSPublicSoap nsnKPSPublicSoap;
    private final OrderRequestServiceClient orderRequestServiceClient;

    public MernisService(NSNKPSPublicSoap nsnKPSPublicSoap, OrderRequestServiceClient orderRequestServiceClient) {
        this.nsnKPSPublicSoap = nsnKPSPublicSoap;
        this.orderRequestServiceClient = orderRequestServiceClient;
    }

    private OrderRequestDto getOrderRequestResponse(UUID orderRequestId) {
        try {
            ResponseEntity<GeneralResponse<OrderRequestDto>> response = this.orderRequestServiceClient.getOrderRequest(orderRequestId);
            log.info("Response received for orderRequestId: {} with status: {}", orderRequestId, response.getStatusCode());
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (Exception e) {
            log.error("Error fetching order request for orderRequestId: {}. Error: {}", orderRequestId, e.getMessage(), e);
            throw new GeneralException("Error fetching order request: " + e.getMessage(), e);
        }
    }

    public ValidMernisResponse checkIfRealPerson(UUID orderRequestId) {
        try {
            OrderRequestDto orderRequestDto = this.getOrderRequestResponse(orderRequestId);
            EngagedPartyDto engagedPartyDto = orderRequestDto.getBaseOrder().getEngagedParty();
            log.info("Validating TCKN for orderRequestId: {}, TCKN: {}", orderRequestId, engagedPartyDto.getTckn());
            boolean isValid = nsnKPSPublicSoap.TCKimlikNoDogrula(
                    engagedPartyDto.getTckn(),
                    engagedPartyDto.getFirstName(),
                    engagedPartyDto.getLastName(),
                    engagedPartyDto.getBirthYear()
            );
            log.info("Mernis validation result for orderRequestId: {} is {}", orderRequestId, isValid);
            return ValidMernisResponse.builder()
                    .isMernisValid(isValid)
                    .build();
        } catch (Exception e) {
            log.error("Mernis validation failed for orderRequestId: {}. Error: {}", orderRequestId, e.getMessage(), e);
            throw new GeneralException("Kimlik dogrulama hatasi: " + e.getMessage(), e);
        }
    }
}