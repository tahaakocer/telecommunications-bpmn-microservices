package com.tahaakocer.externalapiservice.service;

import com.tahaakocer.commondto.order.AddressDto;
import com.tahaakocer.commondto.order.EngagedPartyDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import com.tahaakocer.externalapiservice.client.OrderRequestServiceClient;
import com.tahaakocer.externalapiservice.dto.GeneralResponse;
import com.tahaakocer.externalapiservice.dto.bbk.FullAddressResponse;
import com.tahaakocer.externalapiservice.dto.bbk.TTAddressResponseDto;
import com.tahaakocer.externalapiservice.exception.GeneralException;
import com.tahaakocer.externalapiservice.mapper.AddressMapper;
import com.tahaakocer.externalapiservice.util.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class BbkService {
    private final RestTemplate restTemplate;
    private final OrderRequestServiceClient orderRequestServiceClient;

    private final AddressMapper addressMapper;

    public BbkService(RestTemplate restTemplate,
                      OrderRequestServiceClient orderRequestServiceClient,
                      AddressMapper addressMapper) {
        this.restTemplate = restTemplate;
        this.orderRequestServiceClient = orderRequestServiceClient;
        this.addressMapper = addressMapper;
    }

    @Value("${services.ttaddress-service.url}")
    private String url;

    public FullAddressResponse getFullAddress(Integer flat) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("flat", flat)
                .queryParam("datatype", "flat");

        String requestUrl = builder.toUriString();
        log.info("Requesting address from URL: {}", requestUrl);

        ResponseEntity<TTAddressResponseDto> response;

        try {
            response = restTemplate.getForEntity(requestUrl, TTAddressResponseDto.class);
            log.info("Response received with status: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("bbk servisinde hata olustu: {}", e.getMessage(), e);
            throw new GeneralException("bbk servisinde hata olustu: " + e.getMessage(), e);
        }

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("Successful response received, mapping to FullAddressResponse");
            if (response.getBody().getAddress() == null) {
                log.error("TT servisten gelen yanıtta address alanı null");
                throw new GeneralException("TT servisten gelen yanıtta address alanı null");
            }
            return this.addressMapper.ttAddressDtoToFullAddressResponse(response.getBody().getAddress());
        } else {
            log.error("TT servisten başarısız cevap: Status Code {}, Body {}",
                    response.getStatusCode(), response.getBody());
            throw new GeneralException("TT servisten cevap alınamıyor: " + response.getStatusCode());
        }
    }
    public OrderRequestResponse updateAddress(UUID orderRequestId) {
        OrderRequestDto orderRequestDto = this.getOrderRequestResponse(orderRequestId);
        EngagedPartyDto engagedPartyDto = orderRequestDto.getBaseOrder().getEngagedParty();
        AddressDto addressDtoFromOrder = orderRequestDto.getBaseOrder().getEngagedParty().getAddress();
        FullAddressResponse fullAddressResponse = this.getFullAddress(addressDtoFromOrder.getFlat());
        addressDtoFromOrder.setCityName(fullAddressResponse.getCityName());
        addressDtoFromOrder.setDistrictName(fullAddressResponse.getDistrictName());
        addressDtoFromOrder.setTownshipName(fullAddressResponse.getTownshipName());
        addressDtoFromOrder.setVillageName(fullAddressResponse.getVillageName());
        addressDtoFromOrder.setNeighborhoodName(fullAddressResponse.getNeighborhoodName());
        addressDtoFromOrder.setStreetName(fullAddressResponse.getStreetName());
        addressDtoFromOrder.setBlokName(fullAddressResponse.getBlokName());
        addressDtoFromOrder.setSiteName(fullAddressResponse.getSiteName());
        addressDtoFromOrder.setBuildingCode(fullAddressResponse.getBuildingCode());
        addressDtoFromOrder.setOutsideDoorCode(fullAddressResponse.getOutsideDoorCode());
        addressDtoFromOrder.setBbk(fullAddressResponse.getBbk());
        addressDtoFromOrder.setFlatNo(fullAddressResponse.getFlatNo());
        addressDtoFromOrder.setUpdateDate(LocalDateTime.now());
        addressDtoFromOrder.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
        addressDtoFromOrder.setFlatNo(fullAddressResponse.getFlatNo());
        engagedPartyDto.setBbk(fullAddressResponse.getBbk());
        engagedPartyDto.setAddress(addressDtoFromOrder);
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setEngagedParty(engagedPartyDto);
        OrderRequestResponse orderRequestResponse = this.callServiceForUpdateAddress(orderRequestId, orderUpdateDto);
        log.info("Order request response: {}", orderRequestResponse.getCode());
        return orderRequestResponse;
    }

    private OrderRequestResponse callServiceForUpdateAddress(UUID orderRequestId, OrderUpdateDto orderUpdateDto) {
        ResponseEntity<GeneralResponse<OrderRequestResponse>> response;
        try {
            response = this.orderRequestServiceClient.updateOrderRequest(orderRequestId, orderUpdateDto);
            log.info("Response received with status: {}", response.getStatusCode());
            return Objects.requireNonNull(response.getBody()).getData();

        } catch (Exception e) {
            log.error("BbkService - order update feign servisinde hata olustu: {}", e.getMessage(), e);
            throw new GeneralException("order update feign servisinde hata olustu: " + e.getMessage(), e);
        }
    }
    private OrderRequestDto getOrderRequestResponse(UUID orderRequestId) {
        ResponseEntity<GeneralResponse<OrderRequestDto>> response;
        try {
            response = this.orderRequestServiceClient.getOrderRequest(orderRequestId);
            log.info("Response received with status: {}", response.getStatusCode());
            return Objects.requireNonNull(response.getBody()).getData();
        }
        catch (Exception e) {
            log.error("BbkService - order get feign servisinde hata olustu: {}", e.getMessage(), e);
            throw new GeneralException("order get feign servisinde hata olustu: " + e.getMessage(), e);
        }
    }
}