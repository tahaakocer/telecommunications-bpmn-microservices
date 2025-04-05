package com.tahaakocer.orderservice.initializer;

import com.tahaakocer.orderservice.dto.initializer.InitializerDto;
import com.tahaakocer.orderservice.model.mongo.BaseOrder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderFactoryRegistry {
    private final ApplicationContext applicationContext;
    private Map<String, OrderFactory<? extends BaseOrder>> factories = new HashMap<>();

    @PostConstruct
    public void init() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(OrderFactory.class));

        Set<BeanDefinition> definitions = scanner.findCandidateComponents("com.tahaakocer.orderservice");

        for (BeanDefinition bd : definitions) {
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());

                OrderFactory<? extends BaseOrder> factory =
                        (OrderFactory<? extends BaseOrder>) applicationContext.getBean(clazz);

                factories.put(factory.getOrderType(), factory);
                log.info("Registered OrderFactory for type: {}", factory.getOrderType());
            } catch (Exception e) {
                log.error("Error registering OrderFactory: {}", bd.getBeanClassName(), e);
            }
        }
    }
    public OrderFactory<? extends BaseOrder> getFactory(String orderType) {
        OrderFactory<? extends BaseOrder> factory = factories.get(orderType);
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported order type: " + orderType);
        }
        return factory;
    }

    public BaseOrder createOrder(InitializerDto initializerDto, String orderType) {
        if (orderType == null || orderType.isEmpty()) {
            throw new IllegalArgumentException("orderType must be specified");
        }

        OrderFactory<? extends BaseOrder> factory = getFactory(orderType);
        return factory.createOrder(initializerDto, orderType);
    }
}