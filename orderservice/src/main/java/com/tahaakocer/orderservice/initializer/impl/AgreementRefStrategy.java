package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.model.AgreementRef;
import com.tahaakocer.orderservice.model.OrderRequest;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class AgreementRefStrategy implements OrderUpdateStrategy {
    private final OrderRequestRepository orderRequestRepository;

    public AgreementRefStrategy(OrderRequestRepository orderRequestRepository) {
        this.orderRequestRepository = orderRequestRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getAgreementRef() != null;
    }

    @Override
    public boolean objectStatus(OrderRequest order) {
        return order.getBaseOrder() != null &&
                order.getBaseOrder().getAgreementRef() != null;
    }

    @Override
    public void update(OrderRequest order, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;

        try {
            order.getBaseOrder().getAgreementRef().setRefAgreementId(updateDTO.getAgreementRef().getRefAgreementId());
            setBaseOrderUpdateProperties(order);
            setOrderRequestUpdateProperties(order);
            this.orderRequestRepository.save(order);
            log.info("Agreement ref updated to " + order.getBaseOrder().getAgreementRef());
        } catch (Exception e) {
            log.error("Agreement Ref update failed: {}", e.getMessage());
            throw new GeneralException("Agremeent Ref update failed: " + e.getMessage());
        }
    }


    @Override
    public void create(OrderRequest order, OrderUpdateDto updateDTO) {
        if(!canHandle(updateDTO)) return;
        try{
            AgreementRef agreementRef = new AgreementRef();
            agreementRef.setId(UUID.randomUUID());
            agreementRef.setRefAgreementId(updateDTO.getAgreementRef().getRefAgreementId());
            order.getBaseOrder().setAgreementRef(agreementRef);
            setBaseOrderCreateProperties(order);
            setOrderRequestCreateProperties(order);
            this.orderRequestRepository.save(order);
            log.info("Agreement ref created: " + order.getBaseOrder().getAgreementRef());

        } catch (Exception e) {
            log.error("Agreement Ref creation failed: {}", e.getMessage());
            throw new GeneralException("Agreement Ref creation failed: " + e.getMessage());
        }
    }
}