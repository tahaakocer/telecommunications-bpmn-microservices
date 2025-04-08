package com.tahaakocer.orderservice.service;

import com.tahaakocer.orderservice.client.ProcessServiceClient;
import com.tahaakocer.orderservice.dto.BpmnFlowRefDto;
import com.tahaakocer.orderservice.dto.CharacteristicDto;
import com.tahaakocer.orderservice.dto.ProductCatalogDto;
import com.tahaakocer.orderservice.dto.initializer.InitializerDto;
import com.tahaakocer.orderservice.dto.process.StartProcessResponse;
import com.tahaakocer.orderservice.dto.order.OrderRequestResponse;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.dto.update.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.exception.NotFoundException;
import com.tahaakocer.orderservice.initializer.OrderFactoryRegistry;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderRequestService {

    private final OrderRequestRepository orderRequestRepository;
    private final OrderFactoryRegistry orderFactoryRegistry;
    private final MongoTemplate mongoTemplate;
    private final OrderRequestMapper orderRequestMapper;
    private final List<OrderUpdateStrategy> updateStrategies;
    private final ProcessServiceClient processServiceClient;

    private final ProductCatalogService productCatalogService;
    public OrderRequestService(OrderRequestRepository orderRequestRepository,
                               OrderFactoryRegistry orderFactoryRegistry,
                               MongoTemplate mongoTemplate,
                               OrderRequestMapper orderRequestMapper,
                               List<OrderUpdateStrategy> updateStrategies,
                               ProcessServiceClient processServiceClient,
                               ProductCatalogService productCatalogService) {
        this.orderRequestRepository = orderRequestRepository;
        this.orderFactoryRegistry = orderFactoryRegistry;
        this.mongoTemplate = mongoTemplate;
        this.orderRequestMapper = orderRequestMapper;
        this.updateStrategies = updateStrategies;
        this.processServiceClient = processServiceClient;
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
        orderRequest.setCreateDate(order.getCreateDate());

        log.info("Created order request: {}", orderRequest);

        try {
            this.orderRequestRepository.save(orderRequest);
            startOrderProcess(orderRequest, orderType);
        } catch (Exception e) {
            log.error("Error starting process for order request ID: {}. Error: {}", orderRequest.getId(), e.getMessage(), e);
            throw new GeneralException("Failed to start process: " + e.getMessage(), e);
        }

        return this.orderRequestMapper.entityToResponse(orderRequest);
    }

    private void startOrderProcess(OrderRequest orderRequest, String processOrderType) {
        log.info("Starting process for order request ID: {}", orderRequest.getId());

        // Süreç değişkenleri hazırlama
        Map<String, Object> variables = prepareProcessVariables(orderRequest);

        ResponseEntity<GeneralResponse<StartProcessResponse>> responseEntity;
        try {
            String processChannel = orderRequest.getChannel();

            responseEntity = processServiceClient.startProcess(
                    processOrderType,
                    processChannel,
                    variables
            );

            if (responseEntity.getBody() == null  || responseEntity.getBody().getData() == null) {
                throw new GeneralException("Process service response is empty or invalid");
            }
        } catch (Exception e) {
            log.error("Failed to start process for order request ID: {}. Error: {}", orderRequest.getId(), e.getMessage(), e);
            throw new GeneralException("Failed to start process: " + e.getMessage(), e);
        }
    }
    private Map<String, Object> prepareProcessVariables(OrderRequest orderRequest) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderRequestId", orderRequest.getId().toString());
        variables.put("channel", orderRequest.getChannel());
        variables.put("orderType", orderRequest.getBaseOrder().getOrderType());
        variables.put("createDate", orderRequest.getCreateDate().toString());
        variables.put("createdBy", orderRequest.getBaseOrder().getCreatedBy());
        return variables;
    }
//    private void saveProcessInfoToOrder(OrderRequest orderRequest, StartProcessResponse processResponse) {
//        orderRequest.getBaseOrder().setBpmnFlowRef(
//                BpmnFlowRef.builder()
//                        .processInstanceId(processResponse.getProcessInstanceId())
//                        .processDefinitionName(processResponse.getProcessDefinitionId())
//                .build()
//        );
//    }

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

    public OrderRequestResponse updateProduct(UUID orderRequestId, String productCatalogCode, boolean willBeDelete) {
        if (willBeDelete) {
            return deleteProduct(orderRequestId, productCatalogCode);
        } else {
            return addProducts(orderRequestId, productCatalogCode);
        }
    }

    private OrderRequestResponse addProducts(UUID orderRequestId, String productCatalogCode) {
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

    private OrderRequestResponse deleteProduct(UUID orderRequestId, String productCatalogCode) {
        OrderRequest orderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new NotFoundException("Order request not found with ID: " + orderRequestId));
        try {
            if(orderRequest.getBaseOrder() instanceof ProductOrder productOrder) {
                productOrder.getProducts().removeIf(product -> product.getMainProductCode().equals(productCatalogCode));
                orderRequestRepository.save(orderRequest);
                log.info("Updated order request: {}", orderRequest);
                return this.orderRequestMapper.entityToResponse(orderRequest);
            }

        } catch (Exception e) {
            log.error("Error deleting product: {}", e.getMessage());
            throw new GeneralException(e.getMessage());
        }
        return this.orderRequestMapper.entityToResponse(orderRequest);
    }

    public OrderRequestResponse updateOrderRequest(UUID orderRequestId, OrderUpdateDto orderUpdateDto) {
        OrderRequest orderRequest = this.orderRequestRepository.findById(orderRequestId).orElseThrow(
                () -> new GeneralException("Order request not found with ID: " + orderRequestId)
        );
        log.info("Updating order request with ID: {}", orderRequestId);

        updateStrategies.forEach(strategy -> {
            if(strategy.canHandle(orderUpdateDto)) {
                if(strategy.objectStatus(orderRequest)) {
                    log.info("Updating order request with strategy: {}", strategy.getClass().getSimpleName());
                    strategy.update(orderRequest, orderUpdateDto);
                } else {
                    log.info("Creating order request with strategy: {}", strategy.getClass().getSimpleName());
                    strategy.create(orderRequest, orderUpdateDto);
                }
            }
        });

         return this.orderRequestMapper.entityToResponse(orderRequest);
    }

    public OrderRequestResponse updateOrderStatus(UUID orderRequestId, BpmnFlowRefDto bpmnFlowRefDto) {
        OrderRequest orderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new NotFoundException("Order request not found with ID: " + orderRequestId)
                );
        BpmnFlowRef bpmnFlowRef = BpmnFlowRef.builder()
                .id(UUID.randomUUID())
                .processInstanceId(bpmnFlowRefDto.getProcessInstanceId())
                .processDefinitionId(bpmnFlowRefDto.getProcessDefinitionId())
                .processBusinessKey(bpmnFlowRefDto.getProcessBusinessKey())
                .build();

        if(orderRequest.getBaseOrder().getBpmnFlowRef() == null) {
            bpmnFlowRef.setId(UUID.randomUUID());
        } else {
            bpmnFlowRef.setId(orderRequest.getBaseOrder().getBpmnFlowRef().getId());
        }
        orderRequest.getBaseOrder().setBpmnFlowRef(bpmnFlowRef);
        orderRequest.getBaseOrder().setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
        orderRequest.getBaseOrder().setUpdateDate(LocalDateTime.now());
        orderRequest.setLastModifiedBy(orderRequest.getBaseOrder().getLastModifiedBy());
        orderRequest.setUpdateDate(orderRequest.getBaseOrder().getUpdateDate());
        orderRequestRepository.save(orderRequest);
        return this.orderRequestMapper.entityToResponse(orderRequest);

    }
}
