package com.tahaakocer.externalapiservice.dto.infrastructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TTInfrastructureDetailDto {
    // Hız öngörü bilgileri
    private Integer adslMesafeBazliHiz;
    private Integer vdslMesafeBazliHiz;
    private Integer adslOrtalamaBinaHizi;
    private Integer vdslOrtalamaBinaHizi;

    // Diğer önemli bilgiler
    private String santralAdi;
    private String santralMudurlukAdi;
    private Integer santralMesafe;
    private String kabinTipi;
    private String greenBrownDurumu;

    // Boş port bilgileri
    private Boolean adslBosPortVar;
    private Boolean vdslBosPortVar;
    private Boolean fiberBosPortVar;
    private Boolean ndslBosPortVar;
    private Boolean nvdslBosPortVar;

    // Santral hizmet bilgileri
    private Boolean adslHizmetVar;
    private Boolean vdslHizmetVar;
    private Boolean fiberHizmetVar;
    private Boolean ndslHizmetVar;
    private Boolean nvdslHizmetVar;

    // IPTV hizmet bilgileri
    private Boolean dslIptvHizmetVar;
    private Boolean fiberIptvHizmetVar;

    // Hata bilgileri
    private Integer hataKodu;
    private String hataMesaji;

    @JsonProperty("data")
    private void unpackNestedData(TTData data) {
        if (data == null || data.getBbkReturn() == null) {
            return;
        }

        TTBbkReturn bbkReturn = data.getBbkReturn();

        // Hata bilgileri
        this.hataKodu = bbkReturn.getHataKod();
        this.hataMesaji = bbkReturn.getHataMesaj();

        // Santral bilgileri
        if (bbkReturn.getSantralAdiOutput() != null) {
            this.santralAdi = bbkReturn.getSantralAdiOutput().getSonuc();
        }

        if (bbkReturn.getSantralMudurlukAdiOutput() != null) {
            this.santralMudurlukAdi = bbkReturn.getSantralMudurlukAdiOutput().getSonuc();
        }

        if (bbkReturn.getSantralMesafeOutput() != null && bbkReturn.getSantralMesafeOutput().getSonuc() != null) {
            try {
                this.santralMesafe = Integer.parseInt(bbkReturn.getSantralMesafeOutput().getSonuc());
            } catch (NumberFormatException e) {
                // Sayısal değer dönüştürülemezse null bırak
            }
        }

        // Kabin ve altyapı tipi
        if (bbkReturn.getKabinTipiOutput() != null) {
            this.kabinTipi = bbkReturn.getKabinTipiOutput().getSonuc();
        }

        if (bbkReturn.getGreenBrownOutput() != null) {
            this.greenBrownDurumu = bbkReturn.getGreenBrownOutput().getSonuc();
        }

        // Boş port bilgileri
        if (bbkReturn.getBosPortOutput() != null) {
            this.adslBosPortVar = "VAR".equals(bbkReturn.getBosPortOutput().getAdslBosPort());
            this.vdslBosPortVar = "VAR".equals(bbkReturn.getBosPortOutput().getVdslBosPort());
            this.fiberBosPortVar = "VAR".equals(bbkReturn.getBosPortOutput().getFiberBosPort());
            this.ndslBosPortVar = "VAR".equals(bbkReturn.getBosPortOutput().getNdslBosPort());
            this.nvdslBosPortVar = "VAR".equals(bbkReturn.getBosPortOutput().getNvdslBosPort());
        }

        // Santral hizmet bilgileri
        if (bbkReturn.getSantralHizmetOutput() != null) {
            this.adslHizmetVar = "VAR".equals(bbkReturn.getSantralHizmetOutput().getAdsl());
            this.vdslHizmetVar = "VAR".equals(bbkReturn.getSantralHizmetOutput().getVdsl());
            this.fiberHizmetVar = "VAR".equals(bbkReturn.getSantralHizmetOutput().getFiber());
            this.ndslHizmetVar = "VAR".equals(bbkReturn.getSantralHizmetOutput().getNdsl());
            this.nvdslHizmetVar = "VAR".equals(bbkReturn.getSantralHizmetOutput().getNvdsl());
        }

        // IPTV hizmet bilgileri
        if (bbkReturn.getIptvHizmetOutput() != null) {
            this.dslIptvHizmetVar = "VAR".equals(bbkReturn.getIptvHizmetOutput().getDslIptvHizmet());
            this.fiberIptvHizmetVar = "VAR".equals(bbkReturn.getIptvHizmetOutput().getFiberIptvHizmet());
        }

        // Hız öngörüleri
        if (bbkReturn.getVerilecekMaxHizOutput() != null &&
                bbkReturn.getVerilecekMaxHizOutput().getHizOngoru() != null &&
                bbkReturn.getVerilecekMaxHizOutput().getHizOngoru().getHizOngoruList() != null) {

            List<HizOngoru> hizOngoruList = bbkReturn.getVerilecekMaxHizOutput().getHizOngoru().getHizOngoruList();

            for (HizOngoru hiz : hizOngoruList) {
                if (hiz.getValue() != null && !hiz.getValue().isEmpty()) {
                    try {
                        Integer intValue = Integer.parseInt(hiz.getValue());

                        if ("ADSL_MESAFE_BAZLI".equals(hiz.getName())) {
                            this.adslMesafeBazliHiz = intValue;
                        } else if ("VDSL_MESAFE_BAZLI".equals(hiz.getName())) {
                            this.vdslMesafeBazliHiz = intValue;
                        } else if ("ADSL_ORTALAMA_BINA_HIZI".equals(hiz.getName())) {
                            this.adslOrtalamaBinaHizi = intValue;
                        } else if ("VDSL_ORTALAMA_BINA_HIZI".equals(hiz.getName())) {
                            this.vdslOrtalamaBinaHizi = intValue;
                        }
                    } catch (NumberFormatException e) {
                        // Sayısal değer dönüştürülemezse ilgili alanı null bırak
                    }
                }
            }
        }
    }

    // İç içe JSON yapısı için yardımcı sınıflar
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TTData {
        @JsonProperty("getBBKReturn")
        private TTBbkReturn bbkReturn;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TTBbkReturn {
        @JsonProperty("hataKod")
        private Integer hataKod;

        @JsonProperty("hataMesaj")
        private String hataMesaj;

        @JsonProperty("santralAdiOutput")
        private SantralOutput santralAdiOutput;

        @JsonProperty("santralMudurlukAdiOutput")
        private SantralOutput santralMudurlukAdiOutput;

        @JsonProperty("santralMesafeOutput")
        private SantralOutput santralMesafeOutput;

        @JsonProperty("kabinTipiOutput")
        private SantralOutput kabinTipiOutput;

        @JsonProperty("greenBrownOutput")
        private SantralOutput greenBrownOutput;

        @JsonProperty("bosPortOutput")
        private BosPortOutput bosPortOutput;

        @JsonProperty("santralHizmetOutput")
        private SantralHizmetOutput santralHizmetOutput;

        @JsonProperty("iptvHizmetOutput")
        private IptvHizmetOutput iptvHizmetOutput;

        @JsonProperty("verilecekMaxHizOutput")
        private VerilecekMaxHizOutput verilecekMaxHizOutput;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SantralOutput {
        @JsonProperty("sonuc")
        private String sonuc;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BosPortOutput {
        @JsonProperty("adslBosPort")
        private String adslBosPort;

        @JsonProperty("vdslBosPort")
        private String vdslBosPort;

        @JsonProperty("fiberBosPort")
        private String fiberBosPort;

        @JsonProperty("ndslBosPort")
        private String ndslBosPort;

        @JsonProperty("nvdslBosPort")
        private String nvdslBosPort;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SantralHizmetOutput {
        @JsonProperty("adsl")
        private String adsl;

        @JsonProperty("vdsl")
        private String vdsl;

        @JsonProperty("fiber")
        private String fiber;

        @JsonProperty("ndsl")
        private String ndsl;

        @JsonProperty("nvdsl")
        private String nvdsl;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IptvHizmetOutput {
        @JsonProperty("dslIptvHizmet")
        private String dslIptvHizmet;

        @JsonProperty("fiberIptvHizmet")
        private String fiberIptvHizmet;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VerilecekMaxHizOutput {
        @JsonProperty("hizOngoru")
        private HizOngoruWrapper hizOngoru;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HizOngoruWrapper {
        @JsonProperty("hizOngoru")
        private List<HizOngoru> hizOngoruList;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HizOngoru {
        @JsonProperty("name")
        private String name;

        @JsonProperty("value")
        private String value;
    }
}