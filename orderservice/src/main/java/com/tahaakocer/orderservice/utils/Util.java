package com.tahaakocer.orderservice.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Util {

    public static String generateRandomCode(String string) {
        LocalDateTime now = LocalDateTime.now();
        Random random = new Random();
        return string + "-" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + random.nextInt(1000);
    }
}
