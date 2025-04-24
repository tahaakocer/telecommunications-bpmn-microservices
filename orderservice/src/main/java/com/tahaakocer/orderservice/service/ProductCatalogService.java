package com.tahaakocer.orderservice.service;

import com.tahaakocer.orderservice.dto.ProductCatalogDto;

import java.util.List;
import java.util.UUID;

public interface ProductCatalogService {
    ProductCatalogDto createProductCatalog(ProductCatalogDto productCatalogDto);

    List<ProductCatalogDto> searchProductCatalogs(String query);

    ProductCatalogDto getProductCatalogById(UUID id);

    void deleteProductCatalogById(UUID id);

    ProductCatalogDto getProductCatalogByCode(String code);

    void deleteProductCatalogByCode(String code);

    List<ProductCatalogDto> getAllProductCatalogs();

    List<ProductCatalogDto> getProductCatalogsByBbk(Integer bbk);

    List<ProductCatalogDto> createProductCatalogBatch(List<ProductCatalogDto> productCatalogDtos);

}
