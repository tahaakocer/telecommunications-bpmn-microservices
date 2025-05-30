package com.tahaakocer.orderservice.itemizer;


import com.tahaakocer.orderservice.model.BaseOrder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OrderItemizable {
    Class<? extends BaseOrder> orderType();
}
