package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.commondto.order.AddressDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.mapper.AddressMapper;
import com.tahaakocer.orderservice.model.Address;
import com.tahaakocer.orderservice.model.OrderRequest;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AddressStrategy implements OrderUpdateStrategy {
    private final AddressMapper addressMapper;
    private final OrderRequestRepository orderRequestRepository;
    private final RestTemplate restTemplate;

    public AddressStrategy(AddressMapper addressMapper,
                           OrderRequestRepository orderRequestRepository,
                           RestTemplate restTemplate) {
        this.addressMapper = addressMapper;
        this.orderRequestRepository = orderRequestRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getAddress() != null;
    }

    @Override
    public boolean objectStatus(OrderRequest order) {
        return order.getBaseOrder().getEngagedParty().getAddress() != null;
    }

    @Override
    public void update(OrderRequest order, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;
        try {
            updateAddressFromDto(order.getBaseOrder().getEngagedParty().getAddress(), updateDTO.getAddress());
            order.getBaseOrder().setLastModifiedBy(order.getBaseOrder().getEngagedParty().getAddress().getLastModifiedBy());
            order.getBaseOrder().setUpdateDate(order.getBaseOrder().getEngagedParty().getAddress().getUpdateDate());
            order.setUpdateDate(order.getBaseOrder().getEngagedParty().getAddress().getUpdateDate());
            order.setLastModifiedBy(order.getBaseOrder().getEngagedParty().getAddress().getLastModifiedBy());
            this.orderRequestRepository.save(order);

        } catch (Exception e) {
            throw new GeneralException("Address update failed: " + e.getMessage());
        }
    }

    @Override
    public void create(OrderRequest order, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;
        AddressDto addressDto;
        Address address;
        try {
            address = new Address();
            addressDto = updateDTO.getAddress();
            updateAddressFromDto(address, addressDto);
            address.setCreatedBy(KeycloakUtil.getKeycloakUsername());
            address.setCreateDate(LocalDateTime.now());
            order.getBaseOrder().getEngagedParty().setAddress(address);
            order.getBaseOrder().setUpdateDate(address.getUpdateDate());
            order.getBaseOrder().setLastModifiedBy(address.getLastModifiedBy());
            order.setLastModifiedBy(address.getLastModifiedBy());
            order.setUpdateDate(address.getUpdateDate());
            this.orderRequestRepository.save(order);
        } catch (Exception e) {
            throw new GeneralException("Address create failed: " + e.getMessage());
        }
    }


    private void updateAddressFromDto(Address target, AddressDto source) {
        this.addressMapper.updateAddressFromDto(target, source);
        target.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
        target.setUpdateDate(LocalDateTime.now());
        log.info("Address updated.");

    }
}
