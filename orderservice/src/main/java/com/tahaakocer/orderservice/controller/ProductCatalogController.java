package com.tahaakocer.orderservice.controller;

import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.dto.ProductCatalogDto;
import com.tahaakocer.orderservice.dto.request.ProductCatalogRequest;
import com.tahaakocer.orderservice.mapper.ProductCatalogMapper;
import com.tahaakocer.orderservice.service.ProductCatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-catalog")
public class ProductCatalogController {
    private final ProductCatalogService productCatalogService;
    private final ProductCatalogMapper productCatalogMapper;

    public ProductCatalogController(ProductCatalogService productCatalogService,
                                    ProductCatalogMapper productCatalogMapper) {
        this.productCatalogService = productCatalogService;
        this.productCatalogMapper = productCatalogMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<ProductCatalogDto>> createProductCatalog(@RequestBody ProductCatalogRequest productCatalogRequest) {
        ProductCatalogDto productCatalogDto = productCatalogMapper.productCatalogRequestToProductCatalogDto(productCatalogRequest);
        ProductCatalogDto productCatalog = productCatalogService.createProductCatalog(productCatalogDto);
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
        List<ProductCatalogDto> productCatalogs = productCatalogService.createProductCatalogBatch(productCatalogDtos);
        return ResponseEntity.ok(GeneralResponse.<List<ProductCatalogDto>>builder()
                .code(200)
                .message("Product Catalog created successfully")
                .data(productCatalogs)
                .build()
        );
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<GeneralResponse<ProductCatalogDto>> getProductCatalogById(@RequestParam String id) {
        ProductCatalogDto productCatalog = productCatalogService.getProductCatalogById(UUID.fromString(id));
        return ResponseEntity.ok(GeneralResponse.<ProductCatalogDto>builder()
                .code(200)
                .message("Product Catalog found successfully")
                .data(productCatalog)
                .build());
    }
    @GetMapping("/get-by-code")
    public ResponseEntity<GeneralResponse<ProductCatalogDto>> getProductCatalogByCode(@RequestParam String code) {
        ProductCatalogDto productCatalog = productCatalogService.getProductCatalogByCode(code);
        return ResponseEntity.ok(GeneralResponse.<ProductCatalogDto>builder()
                .code(200)
                .message("Product Catalog found successfully")
                .data(productCatalog)
                .build());
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<GeneralResponse<Void>> deleteProductCatalogById(@RequestParam String id) {
        productCatalogService.deleteProductCatalogById(UUID.fromString(id));
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("Product Catalog deleted successfully")
                .data(null)
                .build());
    }
    @DeleteMapping("/delete-by-code")
    public ResponseEntity<GeneralResponse<Void>> deleteProductCatalogByCode(@RequestParam String code) {
        productCatalogService.deleteProductCatalogByCode(code);
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("Product Catalog deleted successfully")
                .data(null)
                .build());
    }
    @GetMapping("/get-all")
    public ResponseEntity<GeneralResponse<List<ProductCatalogDto>>> getAllProductCatalogs() {
        List<ProductCatalogDto> productCatalog = productCatalogService.getAllProductCatalogs();
        return ResponseEntity.ok(GeneralResponse.<List<ProductCatalogDto>>builder()
                .code(200)
                .message("Product Catalog found successfully")
                .data(productCatalog)
                .build());
    }
    @GetMapping("/get-by-bbk")
    public ResponseEntity<GeneralResponse<List<ProductCatalogDto>>> getProductCatalogByBbk(@RequestParam Integer bbk) {
        List<ProductCatalogDto> productCatalog = productCatalogService.getProductCatalogsByBbk(bbk);
        return ResponseEntity.ok(GeneralResponse.<List<ProductCatalogDto>>builder()
                .code(200)
                .message("Product Catalog found successfully")
                .data(productCatalog)
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<GeneralResponse<List<ProductCatalogDto>>> searchProductCatalogs(@RequestParam String query) {
        List<ProductCatalogDto> productCatalog = productCatalogService.searchProductCatalogs(query);
        return ResponseEntity.ok(GeneralResponse.<List<ProductCatalogDto>>builder()
                .code(200)
                .message("Product Catalog searched successfully")
                .data(productCatalog)
                .build());
    }
}
