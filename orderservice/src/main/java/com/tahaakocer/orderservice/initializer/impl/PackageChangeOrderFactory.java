package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.orderservice.dto.initializer.InitializerDto;
import com.tahaakocer.orderservice.dto.initializer.PackageChangeInitializerDto;
import com.tahaakocer.orderservice.dto.initializer.ProductInitializerDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderFactory;
import com.tahaakocer.orderservice.model.mongo.ProductOrder;
import org.springframework.stereotype.Component;

@Component
public class PackageChangeOrderFactory implements OrderFactory<ProductOrder> {

    @Override
    public String getOrderType() {
        return "PACKAGE_CHANGE";
    }

    @Override
    public ProductOrder createOrder(InitializerDto initializerDto, String orderType) {
        PackageChangeInitializerDto packageChangeDto = initializerDto.getPackageChange();

        if (packageChangeDto == null) {
            throw new GeneralException("Product initializer data is required for PRODUCT order type");
        }


        return null;
    }
}