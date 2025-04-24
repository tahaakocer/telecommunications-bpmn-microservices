package com.tahaakocer.orderservice.service.impl;

import com.tahaakocer.orderservice.dto.AddonDto;
import com.tahaakocer.orderservice.dto.ProductCatalogDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.mapper.AddonMapper;
import com.tahaakocer.orderservice.model.mongo.Addon;
import com.tahaakocer.orderservice.repository.mongo.AddonRepository;
import com.tahaakocer.orderservice.service.AddonService;
import com.tahaakocer.orderservice.service.ProductCatalogService;
import com.tahaakocer.orderservice.utils.DeepMergerUtil;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class AddonServiceImpl implements AddonService {
    private final AddonRepository addonRepository;
    private final AddonMapper addonMapper;
    private final DeepMergerUtil deepMergerUtil;
    private final ProductCatalogService productCatalogService;

    public AddonServiceImpl(AddonRepository addonRepository,
                            AddonMapper addonMapper,

                            DeepMergerUtil deepMergerUtil, @Lazy ProductCatalogService productCatalogService) {
        this.addonRepository = addonRepository;
        this.addonMapper = addonMapper;
        this.deepMergerUtil = deepMergerUtil;
        this.productCatalogService = productCatalogService;
    }

    @Override
    public AddonDto createAddon(AddonDto addonDto) {
        boolean isMandatory = addonDto.isMandatory();
        ProductCatalogDto productCatalogDto = this.productCatalogService.getProductCatalogById(addonDto.getMainProductId());
        if (productCatalogDto == null) {
            throw new GeneralException("Product Catalog (Common) not found");
        }

        ProductCatalogDto addonDtoFromDb = this.productCatalogService.getProductCatalogById(addonDto.getAddonProductId());
        if (addonDtoFromDb == null) {
            throw new GeneralException("Product Catalog (Addon) not found");
        } else if (!Objects.equals(addonDtoFromDb.getProductConfType(), "ADDON")) {
            throw new GeneralException("Product Catalog (Addon) not found");
        }

        // MapStruct'ı devre dışı bırakıp manuel util'i kullan
        if (addonDto.getAddonProduct() != null) {
            // Özel deep merger util ile merge işlemini gerçekleştir
            deepMergerUtil.mergeProductCatalogs(addonDtoFromDb, addonDto.getAddonProduct());
        }

        addonDto.setId(UUID.randomUUID());
        addonDto.setAddonProduct(addonDtoFromDb);
        addonDto.setCreatedBy(KeycloakUtil.getKeycloakUsername());
        addonDto.setCreateDate(LocalDateTime.now());
        addonDto.setUpdateDate(addonDto.getCreateDate());
        addonDto.setLastModifiedBy(addonDto.getCreatedBy());
        addonDto.setMandatory(isMandatory);

        Addon saved = this.addonRepository.save(this.addonMapper.addonDtoToAddon(addonDto));
        return this.addonMapper.addonToAddonDto(saved);
    }

    @Override
    public List<AddonDto> getAllAddons() {
        List<Addon> addons = this.addonRepository.findAll();
        log.info("Addons found with count: {}", addons.size());
        return this.addonMapper.addonsToAddonDtos(addons);
    }

    @Override
    public void deleteAddonById(UUID uuid) {
        Addon addon = this.addonRepository.findById(uuid).orElseThrow(
                () -> new GeneralException("Addon not found"));
        log.info("Addon deleted with id: {}", addon.getId());
        this.addonRepository.deleteById(uuid);
    }

    @Override
    public List<AddonDto> getAddonByMainProductId(UUID uuid) {
        List<Addon> addons = this.addonRepository.findByMainProductId(uuid);
        log.info("Addons found with count: {}", addons.size());
        return this.addonMapper.addonsToAddonDtos(addons);
    }
}