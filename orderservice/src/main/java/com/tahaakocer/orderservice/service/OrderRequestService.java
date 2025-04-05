package com.tahaakocer.orderservice.service;

import com.tahaakocer.orderservice.dto.CharacteristicDto;
import com.tahaakocer.orderservice.dto.ProductCatalogDto;
import com.tahaakocer.orderservice.dto.initializer.InitializerDto;
import com.tahaakocer.orderservice.dto.request.OrderRequestResponse;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.exception.NotFoundException;
import com.tahaakocer.orderservice.initializer.OrderFactoryRegistry;
import com.tahaakocer.orderservice.mapper.OrderRequestMapper;
import com.tahaakocer.orderservice.model.mongo.*;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import com.tahaakocer.orderservice.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderRequestService {

    private final OrderRequestRepository orderRequestRepository;
    private final OrderFactoryRegistry orderFactoryRegistry;
    private final MongoTemplate mongoTemplate;
    private final OrderRequestMapper orderRequestMapper;

    private final ProductCatalogService productCatalogService;
    public OrderRequestService(OrderRequestRepository orderRequestRepository,
                               OrderFactoryRegistry orderFactoryRegistry,
                               MongoTemplate mongoTemplate,
                               OrderRequestMapper orderRequestMapper, ProductCatalogService productCatalogService) {
        this.orderRequestRepository = orderRequestRepository;
        this.orderFactoryRegistry = orderFactoryRegistry;
        this.mongoTemplate = mongoTemplate;
        this.orderRequestMapper = orderRequestMapper;
        this.productCatalogService = productCatalogService;
    }

    public OrderRequestResponse createOrderRequest(InitializerDto initializerDto, String orderType, String channel) {
        log.info("Creating order request for order type: {} and channel {}", orderType, channel);
        BaseOrder order = this.orderFactoryRegistry.createOrder(initializerDto, orderType);
        order.setCreatedBy(KeycloakUtil.getKeycloakUsername());
        order.setCreateDate(LocalDateTime.now());
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setChannel(channel);
        orderRequest.setBaseOrder(order);
        orderRequest.setCreatedBy(order.getCreatedBy());
        orderRequest.setCreateDate(order.getCreateDate());
        this.orderRequestRepository.save(orderRequest);

        log.info("Created order request: {}", orderRequest);
        return this.orderRequestMapper.entityToResponse(orderRequest);
    }
    public OrderRequestResponse updateOrderField(UUID orderRequestId, String fieldPath, Object value) {
        log.info("Updating order field {} for order request ID: {}", fieldPath, orderRequestId);

        OrderRequest orderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new NotFoundException("Order request not found with ID: " + orderRequestId));

        Query query = new Query(Criteria.where("id").is(orderRequestId));
        Update update = new Update().set(fieldPath, value);
        update.set("updateDate", LocalDateTime.now());
        update.set("lastModifiedBy", KeycloakUtil.getKeycloakUsername());

        mongoTemplate.updateFirst(query, update, OrderRequest.class);

        OrderRequest savedOrderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new NotFoundException("Order request not found with ID: " + orderRequestId));

        OrderRequestResponse updatedOrder = this.orderRequestMapper.entityToResponse(savedOrderRequest);
        log.info("Updated order request: {}", updatedOrder);
        return updatedOrder;
    }

    public OrderRequestResponse updateOrderFields(UUID orderRequestId, Map<String, Object> fieldsToUpdate) {
        log.info("Updating multiple fields for order request ID: {}", orderRequestId);

        OrderRequest orderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new NotFoundException("Order request not found with ID: " + orderRequestId));

        Query query = new Query(Criteria.where("id").is(orderRequestId));
        Update update = new Update();

        for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }

        update.set("updateDate", LocalDateTime.now());
        update.set("lastModifiedBy", KeycloakUtil.getKeycloakUsername());

        mongoTemplate.updateFirst(query, update, OrderRequest.class);
        OrderRequest savedOrderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new NotFoundException("Order request not found with ID: " + orderRequestId));

        OrderRequestResponse updatedOrder = this.orderRequestMapper.entityToResponse(savedOrderRequest);
        log.info("Updated order request: {}", updatedOrder);
        return updatedOrder;
    }

    public OrderRequestResponse getOrderRequest(UUID orderRequestId) {
        OrderRequest orderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new NotFoundException("Order request not found with ID: " + orderRequestId));
        return this.orderRequestMapper.entityToResponse(orderRequest);
    }

    public OrderRequestResponse addProducts(UUID orderRequestId, String productCatalogCode) {
        ProductCatalogDto productCatalogDto = this.productCatalogService.getProductCatalogByCode(productCatalogCode);
        OrderRequest orderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new NotFoundException("Order request not found with ID: " + orderRequestId));
        try {
            ProductOrder productorder = (ProductOrder) orderRequest.getBaseOrder();
            List<Product> products = productorder.getProducts();
            if(products == null) products = new ArrayList<>();
            Product product = new Product();
            product.setName(productCatalogDto.getName());
            List<CharacteristicDto> characteristicDtos = productCatalogDto.getSpecification().getCharacteristics();
            product.setCharacteristics(characteristicDtos.stream().map(characteristicDto ->
                    Characteristic.builder()
                            .id(characteristicDto.getId())
                            .createDate(LocalDateTime.now())
                            .updateDate(LocalDateTime.now())
                            .createdBy(KeycloakUtil.getKeycloakUsername())
                            .lastModifiedBy(KeycloakUtil.getKeycloakUsername())
                            .code(characteristicDto.getCode())
                            .name(characteristicDto.getName())
                            .value(characteristicDto.getValue())
                            .sourceType(characteristicDto.getSourceType())
                            .build()

            ).collect(Collectors.toList()));
            product.setCode(Util.generateRandomCode("PRD"));
            product.setMainProductCode(productCatalogDto.getCode());
            product.setProductCatalogId(productCatalogDto.getId());
            product.setProductType(productCatalogDto.getProductType());
            product.setProductConfType(productCatalogDto.getProductConfType());
            product.setCreateDate(LocalDateTime.now());
            product.setCreatedBy(KeycloakUtil.getKeycloakUsername());
            product.setUpdateDate(LocalDateTime.now());
            product.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            products.add(product);
            ((ProductOrder) orderRequest.getBaseOrder()).setProducts(products);
            orderRequestRepository.save(orderRequest);
            log.info("Updated order request: {}", orderRequest);
        } catch (Exception e) {
            throw new GeneralException(e.getMessage());
        }

        return this.orderRequestMapper.entityToResponse(orderRequest);
    }

}
