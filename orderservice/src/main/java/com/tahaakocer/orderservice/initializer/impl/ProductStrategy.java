package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.orderservice.dto.ProductDto;
import com.tahaakocer.orderservice.dto.update.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.mapper.ProductMapper;
import com.tahaakocer.orderservice.model.mongo.Product;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.model.mongo.ProductOrder;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductStrategy implements OrderUpdateStrategy {
    private final ProductMapper productMapper;
    private final OrderRequestRepository orderRequestRepository;

    public ProductStrategy(ProductMapper productMapper,
                           OrderRequestRepository orderRequestRepository) {
        this.productMapper = productMapper;
        this.orderRequestRepository = orderRequestRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getProducts() != null && !updateDTO.getProducts().isEmpty();
    }

    @Override
    public boolean objectStatus(OrderRequest order) {
        return order.getBaseOrder() instanceof ProductOrder &&
                ((ProductOrder) order.getBaseOrder()).getProducts() != null &&
                !((ProductOrder) order.getBaseOrder()).getProducts().isEmpty();
    }

    @Override
    public void update(OrderRequest order, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;

        try {
            if (!(order.getBaseOrder() instanceof ProductOrder)) {
                log.error("BaseOrder is not a ProductOrder");
                throw new GeneralException("BaseOrder is not a ProductOrder");
            }

            ProductOrder productOrder = (ProductOrder) order.getBaseOrder();
            List<Product> existingProducts = productOrder.getProducts();
            if (existingProducts == null) {
                existingProducts = new ArrayList<>();
                productOrder.setProducts(existingProducts);
            }

            List<ProductDto> updatedProducts = updateDTO.getProducts();

            Map<UUID, Product> productMap = existingProducts.stream()
                    .collect(Collectors.toMap(Product::getId, Function.identity()));

            for (ProductDto productDto : updatedProducts) {
                if (productDto.getId() != null && productMap.containsKey(productDto.getId())) {
                    Product existingProduct = productMap.get(productDto.getId());
                    updateProductFromDto(existingProduct, productDto);
                } else {
                    Product newProduct = new Product();
                    newProduct.setId(UUID.randomUUID());
                    updateProductFromDto(newProduct, productDto);
                    newProduct.setCreatedBy(KeycloakUtil.getKeycloakUsername());
                    newProduct.setCreateDate(LocalDateTime.now());
                    existingProducts.add(newProduct);
                }
            }

            if (updateDTO.isRemoveUnlistedProducts()) {
                List<UUID> updatedIds = updatedProducts.stream()
                        .map(ProductDto::getId)
                        .filter(Objects::nonNull)
                        .toList();

                existingProducts.removeIf(product ->
                        product.getId() != null && !updatedIds.contains(product.getId()));
            }

            updateBaseProperties(order);
            log.info("Products updated successfully");
        } catch (Exception e) {
            log.error("Product update failed: {}", e.getMessage());
            throw new GeneralException("Product update failed: " + e.getMessage());
        }
    }

    @Override
    public void create(OrderRequest order, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;

        try {
            if (!(order.getBaseOrder() instanceof ProductOrder)) {
                log.error("BaseOrder is not a ProductOrder");
                throw new GeneralException("BaseOrder is not a ProductOrder");
            }

            ProductOrder productOrder = (ProductOrder) order.getBaseOrder();
            List<Product> products = new ArrayList<>();

            for (ProductDto productDto : updateDTO.getProducts()) {
                Product product = new Product();
                product.setId(UUID.randomUUID());
                updateProductFromDto(product, productDto);
                product.setCreatedBy(KeycloakUtil.getKeycloakUsername());
                product.setCreateDate(LocalDateTime.now());
                products.add(product);
            }

            productOrder.setProducts(products);
            updateBaseProperties(order);
            log.info("Products created successfully");
        } catch (Exception e) {
            log.error("Product creation failed: {}", e.getMessage());
            throw new GeneralException("Product creation failed: " + e.getMessage());
        }
    }

    private void updateBaseProperties(OrderRequest orderRequest) {
        LocalDateTime now = LocalDateTime.now();
        String username = KeycloakUtil.getKeycloakUsername();

        orderRequest.getBaseOrder().setUpdateDate(now);
        orderRequest.getBaseOrder().setLastModifiedBy(username);
        orderRequest.setUpdateDate(now);
        orderRequest.setLastModifiedBy(username);

        this.orderRequestRepository.save(orderRequest);
    }

    private void updateProductFromDto(Product target, ProductDto source) {
        this.productMapper.updateProductFromDto(target, source);
        target.setUpdateDate(LocalDateTime.now());
        target.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
    }
}