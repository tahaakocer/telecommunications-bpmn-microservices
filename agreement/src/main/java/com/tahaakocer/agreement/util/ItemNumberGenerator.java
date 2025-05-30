package com.tahaakocer.agreement.util;

import java.util.concurrent.atomic.AtomicInteger;

public class ItemNumberGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static int getNext() {
        return counter.incrementAndGet();
    }
}