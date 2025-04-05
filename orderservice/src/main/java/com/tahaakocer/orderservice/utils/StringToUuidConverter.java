package com.tahaakocer.orderservice.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.UUID;

@ReadingConverter
public class StringToUuidConverter implements Converter<String, UUID> {
    @Override
    public UUID convert(String source) {
        try {
            return UUID.fromString(source);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}