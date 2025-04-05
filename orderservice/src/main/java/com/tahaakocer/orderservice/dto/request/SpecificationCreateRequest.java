package com.tahaakocer.orderservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationCreateRequest {

    private String code;
    private List<String> characteristicCodeList;


}
