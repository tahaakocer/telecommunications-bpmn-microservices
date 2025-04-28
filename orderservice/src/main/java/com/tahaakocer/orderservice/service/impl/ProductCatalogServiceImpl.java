package com.tahaakocer.orderservice.service.impl;

import com.tahaakocer.commondto.order.AddonDto;
import com.tahaakocer.commondto.order.ProductCatalogDto;
import com.tahaakocer.commondto.order.SpecificationDto;
import com.tahaakocer.orderservice.client.InfrastructureServiceClient;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.dto.response.MaxSpeedResponse;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.mapper.ProductCatalogMapper;
import com.tahaakocer.orderservice.mapper.SpecificationMapper;
import com.tahaakocer.orderservice.model.mongo.ProductCatalog;
import com.tahaakocer.orderservice.repository.mongo.ProductCatalogRepository;
import com.tahaakocer.orderservice.service.AddonService;
import com.tahaakocer.orderservice.service.ProductCatalogService;
import com.tahaakocer.orderservice.service.SpecificationService;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductCatalogServiceImpl implements ProductCatalogService {
    private final ProductCatalogMapper productCatalogMapper;
    private final ProductCatalogRepository productCatalogRepository;
    private final SpecificationService specificationService;
    private final InfrastructureServiceClient infrastructureServiceClient;

    private final AddonService addonService;
    private final SpecificationMapper specificationMapper;

    public ProductCatalogServiceImpl(ProductCatalogMapper productCatalogMapper,
                                     ProductCatalogRepository productCatalogRepository,
                                     SpecificationService specificationService,
                                     InfrastructureServiceClient infrastructureServiceClient,
                                     @Lazy AddonService addonService,
                                     SpecificationMapper specificationMapper) {
        this.productCatalogMapper = productCatalogMapper;
        this.productCatalogRepository = productCatalogRepository;
        this.specificationService = specificationService;
        this.infrastructureServiceClient = infrastructureServiceClient;
        this.addonService = addonService;
        this.specificationMapper = specificationMapper;
    }

    @Transactional
    public ProductCatalogDto createProductCatalog(ProductCatalogDto productCatalogDto) {
        if (productCatalogRepository.existsByCode(productCatalogDto.getCode())) {
            throw new GeneralException("Product Catalog with code " + productCatalogDto.getCode() + " already exists");
        }

        List<SpecificationDto> specificationDtos = new ArrayList<>();
        final AtomicReference<String> productTypeRef = new AtomicReference<>();

        for(SpecificationDto spec : productCatalogDto.getSpecifications()) {
            SpecificationDto specFromService = this.specificationService.getSpecificationByCode(spec.getCode());

            specFromService.getCharacteristics().forEach(serviceCharacteristic -> {
                        if (serviceCharacteristic.getCode().equals("productType")) {
                            productTypeRef.set((String) serviceCharacteristic.getValue());
                        }

                        spec.getCharacteristics().stream()
                                .filter(inputCharacteristic ->
                                        Objects.equals(serviceCharacteristic.getCode(), inputCharacteristic.getCode()))
                                .findFirst()
                                .ifPresent(matchingCharacteristic ->
                                        serviceCharacteristic.setValue(matchingCharacteristic.getValue()));
                    }
            );
            specificationDtos.add(specFromService);
        }

        String productType = productTypeRef.get();

        ProductCatalog productCatalog = ProductCatalog.builder()
                .id(UUID.randomUUID())
                .code(productCatalogDto.getCode())
                .name(productCatalogDto.getName())
                .specifications(this.specificationMapper.dtoToEntityList(specificationDtos))
                .productType(productType)
                .productConfType(productCatalogDto.getProductConfType())
                .createdBy(KeycloakUtil.getKeycloakUsername())
                .createDate(LocalDateTime.now())
                .lastModifiedBy(productCatalogDto.getCreatedBy())
                .updateDate(productCatalogDto.getCreateDate())
                .build();

        productCatalog = productCatalogRepository.save(productCatalog);
        log.info("Product Catalog created with id: {}", productCatalog.getId());
        return productCatalogMapper.productCatalogToProductCatalogDto(productCatalog);
    }

    public List<ProductCatalogDto> searchProductCatalogs(String query) {
        List<ProductCatalog> productCatalogs = productCatalogRepository.findByProductCatalogCodeAndNameContainingIgnoreCase(query);
        log.info("Product Catalogs found: {}", productCatalogs.size());
        return productCatalogMapper.productCatalogsToProductCatalogDtos(productCatalogs);
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

        List<AddonDto> addonDto = this.addonService.getAddonByMainProductId(productCatalog.getId());

        if(!addonDto.isEmpty()) {
            addonDto.forEach(addon -> this.addonService.deleteAddonById(addon.getId()));
            log.info("Addons deleted with id: {}", productCatalog.getId());
        }
        log.info("Product Catalog deleted with id: {}", productCatalog.getId());
        productCatalogRepository.delete(productCatalog);
    }
    public ProductCatalogDto getProductCatalogByCode(String code) {
        ProductCatalog productCatalog = productCatalogRepository.findByCode(code).orElseThrow(
                () -> new GeneralException("Product Catalog not found"));
        log.info("Product Catalog found with code: {}", productCatalog.getCode());
        return productCatalogMapper.productCatalogToProductCatalogDto(productCatalog);
    }
    public List<ProductCatalogDto> getProductCatalogsByProductConfType(String productConfType) {
        List<ProductCatalog> productCatalogs = productCatalogRepository.findByProductConfType(productConfType);
        if(productCatalogs.isEmpty()) {
            log.info("Product Catalogs found: {}", productCatalogs.size());
            throw new GeneralException("Product Catalog not found");
        }
        return productCatalogMapper.productCatalogsToProductCatalogDtos(productCatalogs);
    }

    public void deleteProductCatalogByCode(String code) {
        ProductCatalog productCatalog = productCatalogRepository.findByCode(code).orElseThrow(
                () -> new GeneralException("Product Catalog not found"));
        log.info("Product Catalog deleted with code: {}", productCatalog.getCode());
        productCatalogRepository.delete(productCatalog);
    }

    public List<ProductCatalogDto> getAllProductCatalogs() {
        List<ProductCatalog> productCatalogs = productCatalogRepository.findAll();
        log.info("Product Catalogs found: {}", productCatalogs.size());
        return productCatalogMapper.productCatalogsToProductCatalogDtos(productCatalogs);
    }

    public List<ProductCatalogDto> getProductCatalogsByBbk(Integer bbk) {
        MaxSpeedResponse maxSpeedResponse = callServiceForMaxSpeed(bbk);
        if(maxSpeedResponse.getVdsl() != null)
            return getProductCatalogsByDownloadSpeedCharacteristic(maxSpeedResponse.getVdsl());
        else if(maxSpeedResponse.getAdsl() != null)
            return getProductCatalogsByDownloadSpeedCharacteristic(maxSpeedResponse.getAdsl());
        else
            throw new GeneralException("altyapıdan dönen adsl ve vdsl degerleri null" + bbk);
    }
    private MaxSpeedResponse callServiceForMaxSpeed(Integer bbk) {
        ResponseEntity<GeneralResponse<MaxSpeedResponse>> response;
        try {
            response = infrastructureServiceClient.maxSpeedFromTT(bbk);
        } catch (Exception e)
        {
            log.error("ProductCatalogServiceImpl - infrastructure feign servisinde hata olustu: {}", e.getMessage(), e);
            throw new GeneralException("ProductCatalogServiceImpl - infrastructure feign servisinde hata olust: " + e.getMessage(), e);
        }
        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody()).getData();
        } else {
            log.error("ProductCatalogServiceImpl - infrastructure feign servisinden cevap alınamıyor: {}", response.getStatusCode());
            throw new GeneralException("ProductCatalogServiceImpl - infrastructure feign servisinden cevap alınamıyor: " + response.getStatusCode());
        }
    }
    private List<ProductCatalogDto> getProductCatalogsByDownloadSpeedCharacteristic(Integer speedValue) {
        List<ProductCatalog> allProductCatalogs = productCatalogRepository.findAll();
        log.info("Tüm ürün katalogları getirildi. Toplam ürün sayısı: {}", allProductCatalogs.size());

        List<ProductCatalog> filteredProductCatalogs = allProductCatalogs.stream()
                .filter(productCatalog -> {
                    return productCatalog.getSpecifications().stream()
                            .anyMatch(specification -> {
                                return specification.getCharacteristics().stream()
                                        .anyMatch(characteristic -> {
                                            if ("downloadSpeed".equals(characteristic.getCode())) {
                                                try {
                                                    String valueStr = String.valueOf(characteristic.getValue());
                                                    int downloadSpeed = Integer.parseInt(valueStr.replaceAll("[^0-9]", ""));
                                                    log.debug("Ürün: {}, indirme hızı: {}", productCatalog.getCode(), downloadSpeed);
                                                    return downloadSpeed < speedValue;
                                                } catch (NumberFormatException e) {
                                                    log.warn("Ürün '{}' için '{}' değeri sayıya dönüştürülemedi: {}",
                                                            productCatalog.getCode(), characteristic.getValue(), e.getMessage());
                                                    return false;
                                                }
                                            }
                                            return false;
                                        });
                            });
                })
                .collect(Collectors.toList());

        log.info("İndirme hızı {}Mbps'den düşük olan {} ürün bulundu", speedValue, filteredProductCatalogs.size());
        return productCatalogMapper.productCatalogsToProductCatalogDtos(filteredProductCatalogs);
    }
    @Transactional
    public List<ProductCatalogDto> createProductCatalogBatch(List<ProductCatalogDto> productCatalogDtos) {
        return productCatalogDtos.stream().map(this::createProductCatalog).toList();
    }
}
