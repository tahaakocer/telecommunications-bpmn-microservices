package com.tahaakocer.account.util;

import com.tahaakocer.account.exception.GeneralException;
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
