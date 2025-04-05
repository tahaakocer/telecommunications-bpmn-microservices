package com.tahaakocer.orderservice.utils;

import com.tahaakocer.orderservice.exception.GeneralException;
import org.springframework.security.core.context.SecurityContextHolder;

public class KeycloakUtil {
    @SuppressWarnings("unchecked")
    public static String getKeycloakUsername() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            throw new GeneralException("username, security context'ten get edilemedi.");
        }
    }
}
