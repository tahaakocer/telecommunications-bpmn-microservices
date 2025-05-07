package com.tahaakocer.crm.service;

import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.crm.client.OrderRequestServiceClient;
import com.tahaakocer.crm.dto.CustomerDto;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.mapper.CustomerMapper;
import com.tahaakocer.crm.model.Customer;
import com.tahaakocer.crm.model.PartyRole;
import com.tahaakocer.crm.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public CustomerService(CustomerRepository customerRepository,
                           IndividualService individualService,
                           PartyRoleService partyRoleService,
                           ContactMediumService contactMediumService,
                           OrderRequestServiceClient orderRequestServiceClient,
                           CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.individualService = individualService;
        this.partyRoleService = partyRoleService;
        this.contactMediumService = contactMediumService;
        this.orderRequestServiceClient = orderRequestServiceClient;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerDto createCustomer(String orderRequestId) {
        OrderRequestDto orderRequestDto = this.callOrderRequestMethod(orderRequestId);
        PartyRole partyRole = partyRoleService.createPartyRoleWithCustomer();

        Customer customer = new Customer();
        customer.setPartyRole(partyRole);
        customer.setHasPersonalDataUsagePerm(true);
        customer.setHasCommunicationPermAppr(true);

        // Önce müşteriyi kaydet
        Customer saved = this.saveCustomer(customer);

        // Sonra diğer ilişkili nesneleri oluştur
        this.individualService.createIndividualWithOrderRequestAndPartyRole(orderRequestDto, partyRole);
        this.contactMediumService.createContactMediumWithOrderAndPartyRole(orderRequestDto, partyRole);

        return this.customerMapper.entityToDto(saved);
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
}