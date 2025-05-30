package com.tahaakocer.orderservice.itemizer.impl;

import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.itemizer.OrderItemizable;
import com.tahaakocer.orderservice.itemizer.OrderItemizer;
import com.tahaakocer.orderservice.model.OrderRequest;
import com.tahaakocer.orderservice.model.PackageChangeOrder;
import com.tahaakocer.orderservice.model.PackageChangeOrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@OrderItemizable(orderType = PackageChangeOrder.class)
public class PackageChangeOrderItemizer implements OrderItemizer<PackageChangeOrderItem> {


    @Override
    public List<PackageChangeOrderItem> itemize(OrderRequest order) {
        if (order == null) {
            throw new GeneralException("Order cannot be null");
        }

        List<PackageChangeOrderItem> items = new ArrayList<>();

        //TODO tamamlanacak

        return items;
    }


}