package com.tahaakocer.orderservice.service;

import com.tahaakocer.commondto.order.AddonDto;

import java.util.List;
import java.util.UUID;

public interface AddonService {
    AddonDto createAddon(AddonDto addonDto);

    List<AddonDto> getAllAddons();

    void deleteAddonById(UUID uuid);

    List<AddonDto> getAddonByMainProductId(UUID uuid);
}
