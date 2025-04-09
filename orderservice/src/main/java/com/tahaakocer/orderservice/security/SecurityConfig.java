package com.tahaakocer.orderservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;

    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(
                                frameOptions -> frameOptions
                                        .sameOrigin()
                                        .defaultsDisabled()
                                        .addHeaderWriter(
                                                new XFrameOptionsHeaderWriter(
                                                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)
                                        )
                        )
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/characteristic/**").hasRole("user")
                        .requestMatchers("/api/initialize/**").hasRole("user")
                        .requestMatchers("/api/specifications/**").hasRole("user")
                        .requestMatchers("/api/product-catalog/**").hasRole("user")
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
