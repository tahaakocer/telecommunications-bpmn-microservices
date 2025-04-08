package com.tahaakocer.camunda.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "process")
public class ProcessMappingConfig {
    private Map<String, Map<String, String>> mapping = new HashMap<>();

}