package com.tahaakocer.externalapiservice.dto.bbk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfrastructureDetailResponse {
    @JsonProperty("MaxSpeed")
    private String maxSpeed;
    
    @JsonProperty("SVUID")
    private String svuid;
    
    @JsonProperty("ADSL")
    private PortInfo adsl;
    
    @JsonProperty("VDSL")
    private PortInfo vdsl;
    
    @JsonProperty("Fiber")
    private PortInfo fiber;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortInfo {
        @JsonProperty("PortState")
        private String portState;
        
        @JsonProperty("Distance")
        private String distance;
    }
}