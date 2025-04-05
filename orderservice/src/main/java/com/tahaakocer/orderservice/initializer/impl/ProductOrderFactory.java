package com.tahaakocer.orderservice.initializer.impl;
import com.tahaakocer.orderservice.dto.initializer.InitializerDto;
import com.tahaakocer.orderservice.dto.initializer.ProductInitializerDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderFactory;
import com.tahaakocer.orderservice.model.mongo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductOrderFactory implements OrderFactory<ProductOrder> {

    @Override
    public String getOrderType() {
        return "PRODUCT";
    }

    @Override
    public ProductOrder createOrder(InitializerDto initializerDto, String orderType) {
        ProductInitializerDto productDto = initializerDto.getProduct();

        if (productDto == null) {
            throw new GeneralException("Product initializer data is required for PRODUCT order type");
        }

        ProductOrder productOrder = new ProductOrder();
        productOrder.setIsDraft(productDto.getIsDraft());
        productOrder.setOrderType(orderType);
        log.info("Created ProductOrder: {}", productOrder);
        return productOrder;
    }
}