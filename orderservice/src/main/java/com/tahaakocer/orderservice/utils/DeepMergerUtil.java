package com.tahaakocer.orderservice.utils;

import com.tahaakocer.commondto.order.CharacteristicDto;
import com.tahaakocer.commondto.order.ProductCatalogDto;
import com.tahaakocer.commondto.order.SpecificationDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * İç içe nesneleri (addonProduct, specification, characteristic) manuel olarak
 * güncelleyen utility sınıfı
 */
@Component
public class DeepMergerUtil {

    /**
     * Source DTO'dan null olmayan değerleri target DTO'ya kopyalar.
     * İç içe nesneler (specifications, characteristics) için özel mantık uygular.
     *
     * @param target Hedef ProductCatalogDto nesnesi
     * @param source Kaynak ProductCatalogDto nesnesi
     */
    public void mergeProductCatalogs(ProductCatalogDto target, ProductCatalogDto source) {
        if (target == null || source == null) {
            return;
        }

        // Basit alanları güncelle (null olmayan değerleri kopyala)
        if (source.getCode() != null) target.setCode(source.getCode());
        if (source.getName() != null) target.setName(source.getName());
        if (source.getProductType() != null) target.setProductType(source.getProductType());
        if (source.getProductConfType() != null) target.setProductConfType(source.getProductConfType());

        // Specifications güncellemesi
        if (source.getSpecifications() != null && !source.getSpecifications().isEmpty()) {
            mergeSpecifications(target, source);
        }
    }

    /**
     * Source DTO'daki specifications'ları target DTO içindeki specification'larla birleştirir.
     * Code değerine göre eşleştirme yapar.
     */
    private void mergeSpecifications(ProductCatalogDto target, ProductCatalogDto source) {
        // Target'ta specification yoksa, boş bir liste oluştur
        if (target.getSpecifications() == null) {
            target.setSpecifications(new ArrayList<>());
        }

        boolean foundMatchingSpec = false;

        // Her kaynak specification için
        for (SpecificationDto sourceSpec : source.getSpecifications()) {
            // Önce source spec'in code'u var mı kontrol et
            String sourceSpecCode = sourceSpec.getCode();

            // Source spec için bir code yok, ama belki characteristics var
            if (sourceSpec.getCharacteristics() != null && !sourceSpec.getCharacteristics().isEmpty()) {
                // Target'taki tüm spec'leri gez
                for (SpecificationDto targetSpec : target.getSpecifications()) {
                    // Her bir targetSpec için iç içe characteristics'leri güncelle
                    if (mergeCharacteristicsBySourceCharacteristics(targetSpec, sourceSpec)) {
                        foundMatchingSpec = true;
                    }
                }
            }
            // Source spec'in code'u var, o zaman code'a göre eşleştir
            else if (sourceSpecCode != null) {
                boolean specFound = false;

                // Target spec'leri gez ve code'a göre eşleştir
                for (SpecificationDto targetSpec : target.getSpecifications()) {
                    if (sourceSpecCode.equals(targetSpec.getCode())) {
                        // Bulunan spec'in characteristics'lerini güncelle
                        mergeCharacteristics(targetSpec, sourceSpec);
                        specFound = true;
                        foundMatchingSpec = true;
                        break;
                    }
                }

                // Eşleşen spec bulunamadıysa, yeni ekle
                if (!specFound) {
                    target.getSpecifications().add(sourceSpec);
                    foundMatchingSpec = true;
                }
            }
        }

        // Hiçbir eşleşme bulunmadıysa, source spec'lerini olduğu gibi ekle
        // (bu genellikle source spec'in code'u yoksa ve hedefte de spec yoksa olur)
        if (!foundMatchingSpec && source.getSpecifications() != null && !source.getSpecifications().isEmpty()) {
            target.getSpecifications().addAll(source.getSpecifications());
        }
    }

    /**
     * Source DTO'daki characteristics'leri target DTO içindeki characteristics'lerle birleştirir.
     * Bu metod, specification code'una bakmaksızın, tüm target spec'lerde characteristic code'una göre güncelleme yapar.
     */
    private boolean mergeCharacteristicsBySourceCharacteristics(SpecificationDto targetSpec, SpecificationDto sourceSpec) {
        boolean anyCharUpdated = false;

        // Source'ta characteristic yoksa işlem yapma
        if (sourceSpec.getCharacteristics() == null || sourceSpec.getCharacteristics().isEmpty()) {
            return false;
        }

        // Target'ta characteristic yoksa, boş bir liste oluştur
        if (targetSpec.getCharacteristics() == null) {
            targetSpec.setCharacteristics(new ArrayList<>());
        }

        // Her bir source characteristic için
        for (CharacteristicDto sourceChar : sourceSpec.getCharacteristics()) {
            if (sourceChar.getCode() == null) continue;

            boolean charFound = false;

            // Target characteristics içinde code'a göre ara
            for (CharacteristicDto targetChar : targetSpec.getCharacteristics()) {
                if (sourceChar.getCode().equals(targetChar.getCode())) {
                    // Varsa, null olmayan değerleri güncelle
                    if (sourceChar.getName() != null) targetChar.setName(sourceChar.getName());
                    if (sourceChar.getValue() != null) targetChar.setValue(sourceChar.getValue());
                    if (sourceChar.getSourceType() != null) targetChar.setSourceType(sourceChar.getSourceType());
                    charFound = true;
                    anyCharUpdated = true;
                    break;
                }
            }

            // Yoksa ve eklenmesi gerekiyorsa ekle
            if (!charFound) {
                targetSpec.getCharacteristics().add(sourceChar);
                anyCharUpdated = true;
            }
        }

        return anyCharUpdated;
    }

    /**
     * Source DTO'daki characteristics'leri target DTO içindeki characteristics'lerle birleştirir.
     * Code değerine göre eşleştirme yapar.
     */
    private void mergeCharacteristics(SpecificationDto targetSpec, SpecificationDto sourceSpec) {
        // Source'ta characteristic yoksa işlem yapma
        if (sourceSpec.getCharacteristics() == null || sourceSpec.getCharacteristics().isEmpty()) {
            return;
        }

        // Target'ta characteristic yoksa, boş bir liste oluştur
        if (targetSpec.getCharacteristics() == null) {
            targetSpec.setCharacteristics(new ArrayList<>());
        }

        // Her bir source characteristic için
        for (CharacteristicDto sourceChar : sourceSpec.getCharacteristics()) {
            if (sourceChar.getCode() == null) continue;

            boolean charFound = false;

            // Target characteristics içinde code'a göre ara
            for (CharacteristicDto targetChar : targetSpec.getCharacteristics()) {
                if (sourceChar.getCode().equals(targetChar.getCode())) {
                    // Varsa, null olmayan değerleri güncelle
                    if (sourceChar.getName() != null) targetChar.setName(sourceChar.getName());
                    if (sourceChar.getValue() != null) targetChar.setValue(sourceChar.getValue());
                    if (sourceChar.getSourceType() != null) targetChar.setSourceType(sourceChar.getSourceType());
                    charFound = true;
                    break;
                }
            }

            // Yoksa, yeni ekle
            if (!charFound) {
                targetSpec.getCharacteristics().add(sourceChar);
            }
        }
    }
}