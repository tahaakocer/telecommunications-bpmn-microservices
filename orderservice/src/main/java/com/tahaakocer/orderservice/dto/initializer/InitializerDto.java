package com.tahaakocer.orderservice.dto.initializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class InitializerDto {
    private ProductInitializerDto product;
    private PackageChangeInitializerDto packageChange;
    private RelocationInitializerDto relocation;
}
