package org.ming.chat.util;

import java.util.Random;

public class RandomUtils {

    private static Random instance = new Random();

    public static long nextLong() {
        return instance.nextLong();
    }
}
