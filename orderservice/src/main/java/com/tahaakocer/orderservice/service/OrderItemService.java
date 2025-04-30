package com.tahaakocer.orderservice.service;

import com.tahaakocer.commondto.order.OrderItemDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.itemizer.OrderItemizer;
import com.tahaakocer.orderservice.itemizer.OrderItemizerFactory;
import com.tahaakocer.orderservice.mapper.OrderItemMapper;
import com.tahaakocer.orderservice.model.mongo.BaseOrderItem;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.orderitemupdater.OrderItemUpdateStrategy;
import com.tahaakocer.orderservice.repository.mongo.OrderItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemizerFactory orderItemizerFactory;
    private final OrderRequestService orderRequestService;
    private final List<OrderItemUpdateStrategy> orderItemUpdateStrategies;
    private final OrderItemMapper orderItemMapper;


    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderItemizerFactory orderItemizerFactory,
                            OrderRequestService orderRequestService,
                            List<OrderItemUpdateStrategy> orderItemUpdateStrategies,
                            OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemizerFactory = orderItemizerFactory;
        this.orderRequestService = orderRequestService;
        this.orderItemUpdateStrategies = orderItemUpdateStrategies;
        this.orderItemMapper = orderItemMapper;
    }
    @SuppressWarnings("unchecked")
    public OrderRequestResponse itemizeOrder(String orderRequestId) {
        OrderRequest orderRequest = orderRequestService.getOrderRequestEntity(UUID.fromString(orderRequestId));
        OrderItemizer<?> itemizer = this.orderItemizerFactory.getItemizer(orderRequest.getBaseOrder());
        if (itemizer != null) {
            List<?> items = itemizer.itemize(orderRequest);
            if (items.isEmpty()) {
                throw new GeneralException("No items found for order request ID: " + orderRequestId);
            }

            orderRequest.getBaseOrder().setOrderItems((List<BaseOrderItem>) items);
            items.forEach(orderItem -> this.saveOrderItem((BaseOrderItem) orderItem));
            OrderRequestResponse orderSaved = orderRequestService.saveOrderRequestEntity(orderRequest);
            log.info("itemizeOrder - Order request saved: " + orderSaved);
            return orderSaved;
        } else {
            throw new GeneralException("No itemizer found for order type: " + orderRequest.getBaseOrder().getOrderType());
        }
    }
    private void saveOrderItem(BaseOrderItem orderItem) {
        try
        {
            if (orderItem == null) {
                throw new GeneralException("Order item cannot be null");
            }
            orderItemRepository.save(orderItem);
        } catch (GeneralException e) {
            log.error("Error saving order item: {}", e.getMessage());
            throw e;
        }
    }
    public OrderItemDto updateOrderItem(UUID orderItemId, OrderUpdateDto updateDto) {
        BaseOrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new GeneralException("Order item not found with ID: " + orderItemId));
        log.info("updateOrderItem - Order item updating: " + orderItemId);

        orderItemUpdateStrategies.forEach(strategy -> {
            if (strategy.canHandle(updateDto)) {
                if (strategy.objectStatus(orderItem)) {
                    log.info("Updating order item with ID: " + orderItemId);
                    strategy.update(orderItem, updateDto);
                } else {
                    log.info("Updating order item with ID: " + orderItemId);
                    strategy.create(orderItem, updateDto);
                }
            }
        });
        return this.orderItemMapper.entityToDto(orderItem);
    }

}
