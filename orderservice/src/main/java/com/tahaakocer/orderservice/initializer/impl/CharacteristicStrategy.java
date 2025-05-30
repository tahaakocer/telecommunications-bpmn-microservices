package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.commondto.order.CharacteristicDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.mapper.CharacteristicMapper;
import com.tahaakocer.orderservice.model.Characteristic;
import com.tahaakocer.orderservice.model.OrderRequest;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import com.tahaakocer.orderservice.utils.PUUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CharacteristicStrategy implements OrderUpdateStrategy {
    private final CharacteristicMapper characteristicMapper;
    private final OrderRequestRepository orderRequestRepository;

    public CharacteristicStrategy(CharacteristicMapper characteristicMapper,
                                  OrderRequestRepository orderRequestRepository) {
        this.characteristicMapper = characteristicMapper;
        this.orderRequestRepository = orderRequestRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getCharacteristics() != null && !updateDTO.getCharacteristics().isEmpty();
    }

    @Override
    public boolean objectStatus(OrderRequest orderRequest) {
        return orderRequest.getBaseOrder().getCharacteristics() != null &&
                !orderRequest.getBaseOrder().getCharacteristics().isEmpty();
    }

    @Override
    public void update(OrderRequest orderRequest, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;

        try {
            List<Characteristic> existingCharacteristics = orderRequest.getBaseOrder().getCharacteristics();
            List<CharacteristicDto> updatedCharacteristics = updateDTO.getCharacteristics();

            // id mapliyoz
            Map<UUID, Characteristic> characteristicMap = existingCharacteristics.stream()
                    .collect(Collectors.toMap(Characteristic::getId, Function.identity()));

            for (CharacteristicDto characteristicDto : updatedCharacteristics) {
                if (characteristicDto.getId() != null && characteristicMap.containsKey(characteristicDto.getId())) {
                    // Güncelleme
                    Characteristic existingCharacteristic = characteristicMap.get(characteristicDto.getId());
                    updateCharacteristicFromDto(existingCharacteristic, characteristicDto);
                } else {
                    // Ekleme
                    Characteristic newCharacteristic = new Characteristic();
                    newCharacteristic.setId(PUUID.randomUUID());
                    updateCharacteristicFromDto(newCharacteristic, characteristicDto);
                    newCharacteristic.setCreatedBy(KeycloakUtil.getKeycloakUsername());
                    newCharacteristic.setCreateDate(LocalDateTime.now());
                    existingCharacteristics.add(newCharacteristic);
                }
            }

            // Silme
            if (updateDTO.isRemoveUnlistedCharacteristics()) {
                List<UUID> updatedIds = updatedCharacteristics.stream()
                        .map(CharacteristicDto::getId)
                        .filter(Objects::nonNull)
                        .toList();

                existingCharacteristics.removeIf(characteristic ->
                        characteristic.getId() != null && !updatedIds.contains(characteristic.getId()));
            }

            updateBaseProperties(orderRequest);
            log.info("Characteristics updated successfully");
        } catch (Exception e) {
            log.error("Characteristic update failed: {}", e.getMessage());
            throw new GeneralException("Characteristic update failed: " + e.getMessage());
        }
    }

    private void updateBaseProperties(OrderRequest orderRequest) {
        LocalDateTime now = LocalDateTime.now();
        String username = KeycloakUtil.getKeycloakUsername();

        orderRequest.getBaseOrder().setUpdateDate(now);
        orderRequest.getBaseOrder().setLastModifiedBy(username);
        orderRequest.setUpdateDate(now);
        orderRequest.setLastModifiedBy(username);

        this.orderRequestRepository.save(orderRequest);
    }

    @Override
    public void create(OrderRequest orderRequest, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;

        try {
            List<Characteristic> characteristics = new ArrayList<>();

            for (CharacteristicDto characteristicDto : updateDTO.getCharacteristics()) {
                Characteristic characteristic = new Characteristic();
                characteristic.setId(PUUID.randomUUID());
                updateCharacteristicFromDto(characteristic, characteristicDto);
                characteristic.setCreatedBy(KeycloakUtil.getKeycloakUsername());
                characteristic.setCreateDate(LocalDateTime.now());
                characteristics.add(characteristic);
            }

            orderRequest.getBaseOrder().setCharacteristics(characteristics);

            updateBaseProperties(orderRequest);
            log.info("Characteristics created successfully");
        } catch (Exception e) {
            log.error("Characteristic creation failed: {}", e.getMessage());
            throw new GeneralException("Characteristic creation failed: " + e.getMessage());
        }
    }
    private void updateCharacteristicFromDto(Characteristic target, CharacteristicDto source) {
        this.characteristicMapper.updateCharacteristicFromDto(target, source);
        target.setUpdateDate(LocalDateTime.now());
        target.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
    }

    public void removeCharacteristic(OrderRequest orderRequest, UUID characteristicId) {
        try {
            List<Characteristic> characteristics = orderRequest.getBaseOrder().getCharacteristics();
            boolean removed = characteristics.removeIf(c -> c.getId().equals(characteristicId));

            if (removed) {
                updateBaseProperties(orderRequest);
                log.info("Characteristic with ID {} removed successfully", characteristicId);
            } else {
                log.warn("Characteristic with ID {} not found", characteristicId);
            }
        } catch (Exception e) {
            log.error("Failed to remove characteristic: {}", e.getMessage());
            throw new GeneralException("Failed to remove characteristic: " + e.getMessage());
        }
    }
}