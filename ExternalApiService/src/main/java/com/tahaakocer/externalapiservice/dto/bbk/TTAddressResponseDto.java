package com.tahaakocer.externalapiservice.dto.bbk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TTAddressResponseDto {
    @JsonProperty("AcikAdres")
    private TTAddressDto address;
}
