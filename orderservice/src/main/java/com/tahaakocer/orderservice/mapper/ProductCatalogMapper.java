package com.tahaakocer.orderservice.mapper;

import com.tahaakocer.commondto.order.CharacteristicDto;
import com.tahaakocer.commondto.order.ProductCatalogDto;
import com.tahaakocer.commondto.order.SpecificationDto;
import com.tahaakocer.orderservice.dto.request.ProductCatalogRequest;
import com.tahaakocer.orderservice.model.ProductCatalog;
import lombok.experimental.SuperBuilder;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = SuperBuilder.class)
public interface ProductCatalogMapper {

    ProductCatalog productCatalogDtoToProductCatalog(ProductCatalogDto productCatalogDto);

    ProductCatalogDto productCatalogToProductCatalogDto(ProductCatalog saved);

    ProductCatalogDto productCatalogRequestToProductCatalogDto(ProductCatalogRequest productCatalogRequest);

    List<ProductCatalogDto> productCatalogsToProductCatalogDtos(List<ProductCatalog> productCatalogs);

    List<ProductCatalogDto> productCatalogRequestToProductCatalogDtoList(List<ProductCatalogRequest> productCatalogRequests);

    /**
     * İç içe koleksiyonları EXCEPT ederek temel özellikleri mapleyecek metod
     * Bu metod, specifications gibi iç içe koleksiyonları güncelleme dışında tutar
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "specifications", ignore = true) // İç içe specification'ları manuel mapleyeceğiz
    void overrideDtoFromDto(@MappingTarget ProductCatalogDto target, ProductCatalogDto source);

    /**
     * Tüm mapleme işlemini manuel olarak gerçekleştiren metod
     * Hem temel özellikleri hem de iç içe özellikleri güncelleyecek
     */
    default void deepMerge(@MappingTarget ProductCatalogDto target, ProductCatalogDto source) {
        if (source == null || target == null) {
            return;
        }

        // Önce basit özellikleri (specifications hariç) maple
        overrideDtoFromDto(target, source);

        // Sonra iç içe specifications ve characteristics'leri maple
        if (source.getSpecifications() != null) {
            updateSpecifications(target, source);
        }
    }

    /**
     * Spesifikasyonları günceller
     */
    default void updateSpecifications(ProductCatalogDto target, ProductCatalogDto source) {
        if (target.getSpecifications() == null) {
            target.setSpecifications(new ArrayList<>());
        }

        if (source.getSpecifications() == null || source.getSpecifications().isEmpty()) {
            return;
        }

        // Target specifikasyonlarını code'a göre map'e dönüştür
        Map<String, SpecificationDto> targetSpecMap = target.getSpecifications().stream()
                .filter(spec -> spec.getCode() != null)
                .collect(Collectors.toMap(SpecificationDto::getCode, Function.identity(), (existing, replacement) -> existing));

        // Source specifikasyonlarını güncelle veya ekle
        List<SpecificationDto> updatedSpecs = new ArrayList<>();
        for (SpecificationDto sourceSpec : source.getSpecifications()) {
            if (sourceSpec.getCode() == null) continue;

            SpecificationDto targetSpec = targetSpecMap.get(sourceSpec.getCode());
            if (targetSpec != null) {
                // Mevcut spesifikasyonu güncelle
                updateCharacteristics(targetSpec, sourceSpec);
                updatedSpecs.add(targetSpec);
            } else {
                // Yeni spesifikasyon ekle
                updatedSpecs.add(sourceSpec);
            }
        }

        // Güncellenmemiş orijinal spesifikasyonları ekle
        for (SpecificationDto targetSpec : target.getSpecifications()) {
            if (targetSpec.getCode() == null || !targetSpecMap.containsKey(targetSpec.getCode())) {
                updatedSpecs.add(targetSpec);
            }
        }

        target.setSpecifications(updatedSpecs);
    }

    /**
     * Karakteristikleri günceller
     */
    default void updateCharacteristics(SpecificationDto targetSpec, SpecificationDto sourceSpec) {
        if (sourceSpec.getCharacteristics() == null || sourceSpec.getCharacteristics().isEmpty()) {
            return;
        }

        if (targetSpec.getCharacteristics() == null) {
            targetSpec.setCharacteristics(new ArrayList<>());
        }

        // Target karakteristiklerini code'a göre map'e dönüştür
        Map<String, CharacteristicDto> targetCharMap = targetSpec.getCharacteristics().stream()
                .filter(ch -> ch.getCode() != null)
                .collect(Collectors.toMap(CharacteristicDto::getCode, Function.identity(), (existing, replacement) -> existing));

        // Source karakteristiklerini güncelle veya ekle
        List<CharacteristicDto> updatedChars = new ArrayList<>();
        for (CharacteristicDto sourceCh : sourceSpec.getCharacteristics()) {
            if (sourceCh.getCode() == null) continue;

            CharacteristicDto targetCh = targetCharMap.get(sourceCh.getCode());
            if (targetCh != null) {
                // Karakteristiği güncelle (null olmayan alanlar)
                if (sourceCh.getName() != null) targetCh.setName(sourceCh.getName());
                if (sourceCh.getValue() != null) targetCh.setValue(sourceCh.getValue());
                if (sourceCh.getSourceType() != null) targetCh.setSourceType(sourceCh.getSourceType());
                updatedChars.add(targetCh);
            } else {
                // Yeni karakteristik ekle
                updatedChars.add(sourceCh);
            }
        }

        // Güncellenmemiş orijinal karakteristikleri ekle
        for (CharacteristicDto targetCh : targetSpec.getCharacteristics()) {
            if (targetCh.getCode() == null || !targetCharMap.containsKey(targetCh.getCode())) {
                updatedChars.add(targetCh);
            }
        }

        targetSpec.setCharacteristics(updatedChars);
    }
}