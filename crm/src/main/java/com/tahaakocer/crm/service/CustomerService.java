package com.tahaakocer.crm.service;

import com.tahaakocer.commondto.crm.CustomerDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.order.PartyRoleRefDto;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import com.tahaakocer.crm.client.OrderRequestServiceClient;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.mapper.CustomerMapper;
import com.tahaakocer.crm.model.*;
import com.tahaakocer.crm.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final IndividualService individualService;
    private final PartyRoleService partyRoleService;
    private final ContactMediumService contactMediumService;
    private final OrderRequestServiceClient orderRequestServiceClient;

    private final CustomerMapper customerMapper;
    private final CharacteristicService characteristicService;
    private final KeycloakService keycloakService;


    public CustomerService(CustomerRepository customerRepository,
                           IndividualService individualService,
                           PartyRoleService partyRoleService,
                           ContactMediumService contactMediumService,
                           OrderRequestServiceClient orderRequestServiceClient,
                           CustomerMapper customerMapper,
                           CharacteristicService characteristicService,
                           KeycloakService keycloakService) {
        this.customerRepository = customerRepository;
        this.individualService = individualService;
        this.partyRoleService = partyRoleService;
        this.contactMediumService = contactMediumService;
        this.orderRequestServiceClient = orderRequestServiceClient;
        this.customerMapper = customerMapper;
        this.characteristicService = characteristicService;
        this.keycloakService = keycloakService;
    }

    @Transactional
    public CustomerDto createCustomer(String orderRequestId) {
        OrderRequestDto orderRequestDto = this.callOrderRequestMethod(orderRequestId);
        PartyRole partyRole = partyRoleService.createPartyRole("CUSTOMER");

        Customer customer = new Customer();
        customer.setPartyRole(partyRole);
        customer.setHasPersonalDataUsagePerm(true);
        customer.setHasCommunicationPermAppr(true);

        // Önce müşteriyi kaydet
        Customer saved = this.saveCustomer(customer);

        // Sonra diğer ilişkili nesneleri oluştur
        Individual savedIndividual = this.individualService.createIndividualWithOrderRequest(orderRequestDto, partyRole);
        List<ContactMedium> savedContactMedia = this.contactMediumService.createContactMediumWithOrder(orderRequestDto, partyRole);
        Characteristic characteristic = new Characteristic();
        characteristic.setName("orderRequestId");
        characteristic.setValue(String.valueOf(orderRequestDto.getId()));
        characteristic.setPartyRole(partyRole);
        partyRole.getCharacteristics().add(characteristic);
        partyRole.setIndividual(savedIndividual);
        partyRole.setContactMedia(savedContactMedia);
        saved.setPartyRole(partyRole);
        this.characteristicService.saveCharacteristic(characteristic);

        PartyRoleRefDto partyRoleRefDto = new PartyRoleRefDto();
        partyRoleRefDto.setRefPartyRoleId(partyRole.getId());

        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setPartyRoleRef(partyRoleRefDto);
        this.callUpdateOrderRequestMethod(orderRequestId,orderUpdateDto);
        return this.customerMapper.entityToDto(saved);
    }
    public CustomerDto createKeycloakUserForCustomer(String orderRequestId) {
        OrderRequestDto orderRequestDto = this.callOrderRequestMethod(orderRequestId);
        String firstName = orderRequestDto.getBaseOrder().getEngagedParty().getFirstName();
        String lastName = orderRequestDto.getBaseOrder().getEngagedParty().getLastName();
        String email = orderRequestDto.getBaseOrder().getEngagedParty().getEmail();

        String keycloakUserId = this.keycloakService.createKeycloakUser(firstName, lastName, email,null);
        if (keycloakUserId == null) {
            throw new GeneralException("Failed to create Keycloak user");
        }
        //TODO : Keycloak kullanıcı bilgilerini CRM sistemine kaydet
        PartyRole partyRole = this.partyRoleService.getPartyRoleEntityByOrderRequestId(orderRequestId);
        if (partyRole == null) {
            throw new GeneralException("PartyRole not found for order request ID: " + orderRequestId);
        }
        Customer customer = this.customerRepository.findByPartyRoleId(partyRole.getId()).orElseThrow(
                () -> new GeneralException("Customer not found for party role ID: " + partyRole.getId())
        );
        customer.setKeycloakUserId(keycloakUserId);
        Characteristic characteristic = new Characteristic();
        characteristic.setName("keycloakUserId");
        characteristic.setValue(keycloakUserId);
        characteristic.setPartyRole(partyRole);
        Characteristic savedChar = this.characteristicService.saveCharacteristic(characteristic);
        partyRole.getCharacteristics().add(savedChar);
        this.saveCustomer(customer);
        return this.customerMapper.entityToDto(customer);
    }
    public CustomerDto getCustomer(String customerId) {
        Customer customer = this.customerRepository.findById(UUID.fromString(customerId))
                .orElseThrow(() -> new GeneralException("Customer not found"));
        return this.customerMapper.entityToDto(customer);
    }

    private Customer saveCustomer(Customer customer) {
        try {
            Customer saved = customerRepository.save(customer);
            log.info("Saving customer " + customer.getPartyRole().getId());
            return saved;
        } catch (Exception e) {
            log.error("Error occurred while saving customer: {}", e.getMessage());
            throw new GeneralException("Failed to save customer");
        }
    }

    private OrderRequestDto callOrderRequestMethod(String orderRequestId) {
        try {
            GeneralResponse<OrderRequestDto> orderRequest = this.orderRequestServiceClient.getOrderRequest(
                    UUID.fromString(orderRequestId)).getBody();

            if (orderRequest == null || orderRequest.getCode() != 200) {
                log.error("Failed to get orderRequest from order service client");
                throw new GeneralException("Failed to get orderRequest from order service client");
            }
            return orderRequest.getData();
        } catch (Exception e) {
            log.error("Error occurred while creating customer: {}", e.getMessage());
            throw new GeneralException("Failed to get orderRequest from order service client");
        }
    }

    private OrderRequestResponse callUpdateOrderRequestMethod(String orderRequestId, OrderUpdateDto orderUpdateDto) {
        try {
            GeneralResponse<OrderRequestResponse> orderRequest = this.orderRequestServiceClient.updateOrderRequest(
                    UUID.fromString(orderRequestId), orderUpdateDto).getBody();

            if (orderRequest == null || orderRequest.getCode() != 200) {
                log.error("Failed to get orderRequest from order service client");
                throw new GeneralException("Failed to get orderRequest from order service client");
            }
            return orderRequest.getData();
        } catch (Exception e) {
            log.error("Error occurred while creating customer: {}", e.getMessage());
            throw new GeneralException("Failed to get orderRequest from order service client");
        }
    }



}