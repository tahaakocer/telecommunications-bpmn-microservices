package com.tahaakocer.externalapiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tahaakocer.commondto.order.AddressDto;
import com.tahaakocer.commondto.order.EngagedPartyDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import com.tahaakocer.externalapiservice.client.OrderRequestServiceClient;
import com.tahaakocer.externalapiservice.dto.GeneralResponse;
import com.tahaakocer.externalapiservice.dto.bbk.AddressDataResponse;
import com.tahaakocer.externalapiservice.dto.bbk.FullAddressResponse;
import com.tahaakocer.externalapiservice.exception.GeneralException;
import com.tahaakocer.externalapiservice.mapper.AddressMapper;
import com.tahaakocer.externalapiservice.util.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class BbkService {
    private final RestTemplate restTemplate;
    private final OrderRequestServiceClient orderRequestServiceClient;
    private final AddressMapper addressMapper;
    private final ObjectMapper objectMapper;

    @Value("${services.netspeed.address-url}")
    private String addressUrl;

    public BbkService(RestTemplate restTemplate,
                      OrderRequestServiceClient orderRequestServiceClient,
                      AddressMapper addressMapper,
                      ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.orderRequestServiceClient = orderRequestServiceClient;
        this.addressMapper = addressMapper;
        this.objectMapper = objectMapper;
    }

    public AddressDataResponse getAddressData(int type, String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("type", String.valueOf(type));
        formData.add("id", id);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        log.info("Requesting address data from URL: {} with type: {} and id: {}", addressUrl, type, id);

        ResponseEntity<AddressDataResponse> response;
        try {
            response = restTemplate.postForEntity(addressUrl, requestEntity, AddressDataResponse.class);
            log.info("Response received with status: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("Netspeed adres servisinde hata oluştu: {}", e.getMessage(), e);
            throw new GeneralException("Netspeed adres servisinde hata oluştu: " + e.getMessage(), e);
        }

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("Successful response received from Netspeed");
            return response.getBody();
        } else {
            log.error("Netspeed servisinden başarısız cevap: Status Code {}, Body {}",
                    response.getStatusCode(), response.getBody());
            throw new GeneralException("Netspeed servisinden cevap alınamıyor: " + response.getStatusCode());
        }
    }

    public FullAddressResponse getFullAddress(String bbkCode) {
        // Bu metot yeni API ile uyumlu şekilde güncellenebilir, şu an için sadece
        // kontroller için geçici yapı oluşturulmuştur
        FullAddressResponse response = new FullAddressResponse();
        response.setBbk(Integer.valueOf(bbkCode));
        // Diğer bilgiler gerekirse burada ilgili API çağrıları yapılabilir
        return response;
    }

    public OrderRequestResponse updateAddress(UUID orderRequestId) {
        OrderRequestDto orderRequestDto = this.getOrderRequestResponse(orderRequestId);
        EngagedPartyDto engagedPartyDto = orderRequestDto.getBaseOrder().getEngagedParty();
        AddressDto addressDtoFromOrder = orderRequestDto.getBaseOrder().getEngagedParty().getAddress();

        String bbkCode = String.valueOf(addressDtoFromOrder.getBbk());
        if (bbkCode == null || bbkCode.isEmpty()) {
            throw new GeneralException("BBK kodu bulunamadı");
        }

        FullAddressResponse fullAddressResponse = this.getFullAddress(bbkCode);

        // Adres bilgilerini güncelle
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
        } catch (Exception e) {
            log.error("BbkService - order get feign servisinde hata olustu: {}", e.getMessage(), e);
            throw new GeneralException("order get feign servisinde hata olustu: " + e.getMessage(), e);
        }
    }
}