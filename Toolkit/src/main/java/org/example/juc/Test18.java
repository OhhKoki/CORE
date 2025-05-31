package org.example.juc;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Test18 {
    public static void main(String[] args) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean currentValue = atomicBoolean.get();
        atomicBoolean.set(false);
        boolean oldValue = atomicBoolean.getAndSet(false);
        boolean success = atomicBoolean.compareAndSet(false, false);
    }
}
