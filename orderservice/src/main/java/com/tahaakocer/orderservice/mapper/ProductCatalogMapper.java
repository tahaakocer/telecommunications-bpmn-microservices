package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.orderservice.dto.ProductCatalogDto;
import com.tahaakocer.orderservice.dto.request.ProductCatalogRequest;
import com.tahaakocer.orderservice.model.mongo.ProductCatalog;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = SuperBuilder.class)
public interface ProductCatalogMapper {

    ProductCatalog productCatalogDtoToProductCatalog(ProductCatalogDto productCatalogDto);

    ProductCatalogDto productCatalogToProductCatalogDto(ProductCatalog saved);

    ProductCatalogDto productCatalogRequestToProductCatalogDto(ProductCatalogRequest productCatalogRequest);

    List<ProductCatalogDto> productCatalogsToProductCatalogDtos(List<ProductCatalog> productCatalogs);

    List<ProductCatalogDto> productCatalogRequestToProductCatalogDtoList(List<ProductCatalogRequest> productCatalogRequests);
}
