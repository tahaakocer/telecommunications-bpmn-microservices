package com.tahaakocer.orderservice.controller;

import com.tahaakocer.commondto.order.ProductCatalogDto;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.dto.request.ProductCatalogRequest;
import com.tahaakocer.orderservice.mapper.ProductCatalogMapper;
import com.tahaakocer.orderservice.service.impl.ProductCatalogServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-catalog")
public class ProductCatalogController {
    private final ProductCatalogServiceImpl productCatalogServiceImpl;
    private final ProductCatalogMapper productCatalogMapper;

    public ProductCatalogController(ProductCatalogServiceImpl productCatalogServiceImpl,
                                    ProductCatalogMapper productCatalogMapper) {
        this.productCatalogServiceImpl = productCatalogServiceImpl;
        this.productCatalogMapper = productCatalogMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<ProductCatalogDto>> createProductCatalog(@RequestBody ProductCatalogRequest productCatalogRequest) {
        ProductCatalogDto productCatalogDto = productCatalogMapper.productCatalogRequestToProductCatalogDto(productCatalogRequest);
        ProductCatalogDto productCatalog = productCatalogServiceImpl.createProductCatalog(productCatalogDto);
        return ResponseEntity.ok(GeneralResponse.<ProductCatalogDto>builder()
                .code(200)
                    .message("Product Catalog created successfully")
                        .data(productCatalog)
                .build()
        );
    }
    @PostMapping("/create-batch")
    public ResponseEntity<GeneralResponse<List<ProductCatalogDto>>> createProductCatalog(@RequestBody List<ProductCatalogRequest> productCatalogRequests) {
        List<ProductCatalogDto> productCatalogDtos = productCatalogMapper.productCatalogRequestToProductCatalogDtoList(productCatalogRequests);
        List<ProductCatalogDto> productCatalogs = productCatalogServiceImpl.createProductCatalogBatch(productCatalogDtos);
        return ResponseEntity.ok(GeneralResponse.<List<ProductCatalogDto>>builder()
                .code(200)
                .message("Product Catalog created successfully")
                .data(productCatalogs)
                .build()
        );
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<GeneralResponse<ProductCatalogDto>> getProductCatalogById(@RequestParam String id) {
        ProductCatalogDto productCatalog = productCatalogServiceImpl.getProductCatalogById(UUID.fromString(id));
        return ResponseEntity.ok(GeneralResponse.<ProductCatalogDto>builder()
                .code(200)
                .message("Product Catalog found successfully")
                .data(productCatalog)
                .build());
    }
    @GetMapping("/get-by-code")
    public ResponseEntity<GeneralResponse<ProductCatalogDto>> getProductCatalogByCode(@RequestParam String code) {
        ProductCatalogDto productCatalog = productCatalogServiceImpl.getProductCatalogByCode(code);
        return ResponseEntity.ok(GeneralResponse.<ProductCatalogDto>builder()
                .code(200)
                .message("Product Catalog found successfully")
                .data(productCatalog)
                .build());
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<GeneralResponse<Void>> deleteProductCatalogById(@RequestParam String id) {
        productCatalogServiceImpl.deleteProductCatalogById(UUID.fromString(id));
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("Product Catalog deleted successfully")
                .data(null)
                .build());
    }
    @DeleteMapping("/delete-by-code")
    public ResponseEntity<GeneralResponse<Void>> deleteProductCatalogByCode(@RequestParam String code) {
        productCatalogServiceImpl.deleteProductCatalogByCode(code);
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("Product Catalog deleted successfully")
                .data(null)
                .build());
    }
    @GetMapping("/get-all")
    public ResponseEntity<GeneralResponse<List<ProductCatalogDto>>> getAllProductCatalogs() {
        List<ProductCatalogDto> productCatalog = productCatalogServiceImpl.getAllProductCatalogs();
        return ResponseEntity.ok(GeneralResponse.<List<ProductCatalogDto>>builder()
                .code(200)
                .message("Product Catalog found successfully")
                .data(productCatalog)
                .build());
    }
    @GetMapping("/get-by-bbk")
    public ResponseEntity<GeneralResponse<List<ProductCatalogDto>>> getProductCatalogByBbk(@RequestParam Integer bbk) {
        List<ProductCatalogDto> productCatalog = productCatalogServiceImpl.getProductCatalogsByBbk(bbk);
        return ResponseEntity.ok(GeneralResponse.<List<ProductCatalogDto>>builder()
                .code(200)
                .message("Product Catalog by bbk found successfully")
                .data(productCatalog)
                .build());
    }
    @GetMapping("/get-by-confType")
    public ResponseEntity<GeneralResponse<List<ProductCatalogDto>>> getProductCatalogByConfType(@RequestParam String confType) {
        List<ProductCatalogDto> productCatalog = productCatalogServiceImpl.getProductCatalogsByProductConfType(confType);
        return ResponseEntity.ok(GeneralResponse.<List<ProductCatalogDto>>builder()
                .code(200)
                .message("Product Catalog by confType found successfully")
                .data(productCatalog)
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<GeneralResponse<List<ProductCatalogDto>>> searchProductCatalogs(@RequestParam String query) {
        List<ProductCatalogDto> productCatalog = productCatalogServiceImpl.searchProductCatalogs(query);
        return ResponseEntity.ok(GeneralResponse.<List<ProductCatalogDto>>builder()
                .code(200)
                .message("Product Catalog searched successfully")
                .data(productCatalog)
                .build());
    }
}
