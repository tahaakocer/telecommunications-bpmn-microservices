package com.tahaakocer.externalapiservice.dto.bbk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDataResponse {
    @JsonProperty("InternalException")
    private String internalException;
    
    @JsonProperty("Data")
    private List<AddressItem> data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressItem {
        @JsonProperty("Code")
        private String code;
        
        @JsonProperty("Name")
        private String name;
    }
}