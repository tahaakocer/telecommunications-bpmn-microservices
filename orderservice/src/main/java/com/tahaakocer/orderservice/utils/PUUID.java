package com.tahaakocer.orderservice.utils;

import java.util.Random;
import java.util.UUID;

public class PUUID {

    private PUUID() {
    }
    public static long randomNumber(int digit) {
        int base = (int) Math.pow(10, digit);
        return (long) (base + Math.random() * base * 9);
    }
    public static UUID randomUUID() {
        Random random = new Random();
        return new UUID(random.nextLong(), random.nextLong());
    }
}
