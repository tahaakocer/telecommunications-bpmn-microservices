package com.tahaakocer.agreement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tahaakocer.agreement.client.OrderRequestServiceClient;
import com.tahaakocer.agreement.model.*;
import com.tahaakocer.agreement.repository.AgreementItemRepository;
import com.tahaakocer.commondto.order.OrderItemDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AgreementItemService {
    private final AgreementItemRepository agreementItemRepository;
    private final ObjectMapper objectMapper;

    public AgreementItemService(AgreementItemRepository agreementItemRepository,
                                ObjectMapper objectMapper) {
        this.agreementItemRepository = agreementItemRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    protected List<AgreementItem> createAgreementItemEntitiesWithOrder(OrderRequestDto orderRequest, Agreement savedAgreement) {
        log.info("Creating agreement items for agreement ID: {} from order request ID: {}",
                savedAgreement.getId(), orderRequest.getId());

        List<OrderItemDto> orderItems = orderRequest.getBaseOrder().getOrderItems();
        log.debug("Processing {} order items", orderItems.size());

        List<AgreementItem> createdItems = orderItems.stream()
                .map(orderItem -> createAgreementItemFromOrderItem(orderItem, savedAgreement))
                .map(this::saveAgreementItemEntity)
                .toList();

        log.info("Successfully created {} agreement items for agreement ID: {}",
                createdItems.size(), savedAgreement.getId());

        return createdItems;
    }

    private AgreementItem createAgreementItemFromOrderItem(OrderItemDto orderItem, Agreement savedAgreement) {
        log.debug("Creating agreement item for product ID: {} ({})",
                orderItem.getProduct().getId(), orderItem.getProduct().getName());

        //AgreementItem
        AgreementItem agreementItem = new AgreementItem();
        agreementItem.setAgreement(savedAgreement);

        //Product
        Product product = createProductFromOrderItem(orderItem);
        product.setAgreementItem(agreementItem);
        agreementItem.setProduct(product);

        //AgreementItemStatus
        AgreementItemStatus status = createPendingStatus(agreementItem);
        agreementItem.setAgreementItemStatus(status);

        //AccountRef
        AgreementItemAccountRef accountRef = createAccountRef(orderItem);
//        accountRef.setAgreementItem(agreementItem);
        agreementItem.setAgreementItemAccountRef(accountRef);

        return agreementItem;
    }

    private Product createProductFromOrderItem(OrderItemDto orderItem) {
        var orderProduct = orderItem.getProduct();
        log.debug("Creating product entity for product: {} (type: {}, confType: {})",
                orderProduct.getName(), orderProduct.getProductType(), orderProduct.getProductConfType());

        Product product = new Product();

        product.setCode(orderProduct.getCode());
        product.setName(orderProduct.getName());
        product.setProductType(orderProduct.getProductType());
        product.setProductConfType(orderProduct.getProductConfType());
        product.setMainProduct(isMainProduct(orderProduct.getProductConfType()));

        // ProductCharacteristic'leri oluştur ve Product ile ilişkilendir
        List<ProductCharacteristic> characteristics = createProductCharacteristics(orderProduct.getCharacteristics(), product);
        product.setProductCharacteristics(characteristics);
//        characteristics.forEach(characteristic -> characteristic.setProduct(product));
        log.debug("Product created with {} characteristics, isMainProduct: {}",
                characteristics.size(), product.isMainProduct());

        return product;
    }

    private boolean isMainProduct(String productConfType) {
        return "COMMON".equals(productConfType);
    }

    private List<ProductCharacteristic> createProductCharacteristics(
            List<com.tahaakocer.commondto.order.CharacteristicDto> characteristics,
            Product product) {
        return characteristics.stream()
                .map(dto -> createProductCharacteristic(dto, product))
                .toList();
    }

    private ProductCharacteristic createProductCharacteristic(
            com.tahaakocer.commondto.order.CharacteristicDto dto,
            Product product) {
        ProductCharacteristic characteristic = new ProductCharacteristic();
        characteristic.setName(dto.getName());
        try {
            Object value = dto.getValue();
            log.debug("Characteristic value: {} (type: {})", value, value != null ? value.getClass().getName() : "null");
            if (value instanceof Number || value instanceof String) {
                characteristic.setValue(value.toString());
            } else if (value != null) {
                characteristic.setValue(this.objectMapper.writeValueAsString(value));
            } else {
                characteristic.setValue(null);
            }
        } catch (Exception e) {
            log.error("Failed to process value for characteristic {}: {}", dto.getName(), e.getMessage());
            characteristic.setValue(null);
        }
        characteristic.setSourceType(dto.getSourceType());
        return characteristic;
    }

    private AgreementItemStatus createPendingStatus(AgreementItem agreementItem) {
        AgreementItemStatus status = new AgreementItemStatus();
//        status.setAgreementItem(agreementItem); // Bidirectional ilişki
        status.setPending(true);
        //ACTIVATED
        status.setStatus("ACTIVATED");
        status.setStatusReason("NEW");
        return status;
    }

    private AgreementItemAccountRef createAccountRef(OrderItemDto orderItem) {
        AgreementItemAccountRef accountRef = new AgreementItemAccountRef();
        accountRef.setRefAccountId(orderItem.getAccountRef().getRefAccountId());
        return accountRef;
    }

    private AgreementItem saveAgreementItemEntity(AgreementItem agreementItem) {
        try {
            AgreementItem savedAgreementItem = agreementItemRepository.save(agreementItem);
            log.info("Agreement item saved successfully with ID: {}", savedAgreementItem.getId());
            return savedAgreementItem;
        } catch (Exception e) {
            log.error("Error saving agreement item: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save agreement item", e);
        }
    }
}