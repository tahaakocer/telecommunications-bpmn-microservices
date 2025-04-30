package com.tahaakocer.orderservice.service;

import com.tahaakocer.orderservice.mapper.ProductMapper;
import com.tahaakocer.orderservice.repository.mongo.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }


}
