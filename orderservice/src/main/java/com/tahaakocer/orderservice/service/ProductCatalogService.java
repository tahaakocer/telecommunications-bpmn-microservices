package com.tahaakocer.orderservice.service;

import com.tahaakocer.orderservice.dto.ProductCatalogDto;
import com.tahaakocer.orderservice.dto.SpecificationDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.mapper.ProductCatalogMapper;
import com.tahaakocer.orderservice.model.mongo.ProductCatalog;
import com.tahaakocer.orderservice.repository.mongo.ProductCatalogRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import com.tahaakocer.orderservice.utils.PUUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class ProductCatalogService {
    private final ProductCatalogMapper productCatalogMapper;
    private final ProductCatalogRepository productCatalogRepository;
    private final SpecificationService specificationService;

    public ProductCatalogService(ProductCatalogMapper productCatalogMapper,
                                 ProductCatalogRepository productCatalogRepository,
                                 SpecificationService specificationService) {
        this.productCatalogMapper = productCatalogMapper;
        this.productCatalogRepository = productCatalogRepository;
        this.specificationService = specificationService;
    }

    public ProductCatalogDto createProductCatalog(ProductCatalogDto productCatalogDto) {
        if(productCatalogRepository.existsByCode(productCatalogDto.getCode())) {
            throw new GeneralException("Product Catalog with code " + productCatalogDto.getCode() + " already exists");
        }
        productCatalogDto.setId(PUUID.randomUUID());
        productCatalogDto.setCreatedBy(KeycloakUtil.getKeycloakUsername());
        productCatalogDto.setCreateDate(LocalDateTime.now());
        SpecificationDto specificationDto = specificationService.getSpecificationByCode(productCatalogDto.getSpecificationCode());
        productCatalogDto.setSpecification(specificationDto);
        ProductCatalog productCatalog = productCatalogMapper.productCatalogDtoToProductCatalog(productCatalogDto);
        ProductCatalog saved = productCatalogRepository.save(productCatalog);
        log.info("Product Catalog saved with id: {}", saved.getId());
        return productCatalogMapper.productCatalogToProductCatalogDto(saved);
    }

    public ProductCatalogDto getProductCatalogById(UUID id) {
        ProductCatalog productCatalog = productCatalogRepository.findById(id).orElseThrow(
                () -> new GeneralException("Product Catalog not found"));
        log.info("Product Catalog found with id: {}", productCatalog.getId());
        return productCatalogMapper.productCatalogToProductCatalogDto(productCatalog);
    }
    public void deleteProductCatalogById(UUID id) {
        ProductCatalog productCatalog = productCatalogRepository.findById(id).orElseThrow(
                () -> new GeneralException("Product Catalog not found"));
        log.info("Product Catalog deleted with id: {}", productCatalog.getId());
        productCatalogRepository.delete(productCatalog);
    }
    public ProductCatalogDto getProductCatalogByCode(String code) {
        ProductCatalog productCatalog = productCatalogRepository.findByCode(code).orElseThrow(
                () -> new GeneralException("Product Catalog not found"));
        log.info("Product Catalog found with code: {}", productCatalog.getCode());
        return productCatalogMapper.productCatalogToProductCatalogDto(productCatalog);
    }

    public void deleteProductCatalogByCode(String code) {
        ProductCatalog productCatalog = productCatalogRepository.findByCode(code).orElseThrow(
                () -> new GeneralException("Product Catalog not found"));
        log.info("Product Catalog deleted with code: {}", productCatalog.getCode());
        productCatalogRepository.delete(productCatalog);
    }
}
