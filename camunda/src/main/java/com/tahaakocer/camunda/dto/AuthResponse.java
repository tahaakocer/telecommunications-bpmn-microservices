package com.tahaakocer.camunda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    @JsonProperty("access_token")
    private String accessToken;
}
