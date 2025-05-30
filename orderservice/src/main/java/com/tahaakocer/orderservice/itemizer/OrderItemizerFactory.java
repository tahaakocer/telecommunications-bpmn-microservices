package com.tahaakocer.orderservice.itemizer;

import com.tahaakocer.orderservice.model.BaseOrder;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderItemizerFactory {
    private final ApplicationContext applicationContext;

    private final Map<Class<? extends BaseOrder>, OrderItemizer<?>> itemizerMap = new HashMap<>();

    public OrderItemizerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @PostConstruct
    public void initItemizerMap() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(OrderItemizable.class);

        for (Object bean : beans.values()) {
            if (bean instanceof OrderItemizer) {
                OrderItemizable annotation = bean.getClass().getAnnotation(OrderItemizable.class);
                if (annotation != null) {
                    itemizerMap.put(annotation.orderType(), (OrderItemizer<?>) bean);
                }
            }
        }
    }

    public OrderItemizer<?> getItemizer(BaseOrder order) {
        OrderItemizer<?> itemizer = itemizerMap.get(order.getClass());

        if (itemizer == null) {
            throw new IllegalArgumentException("Unsupported order type: " + order.getClass().getName());
        }

        return itemizer;
    }
}