package com.nagisazz.util;

import java.util.UUID;

public class UUIDUtil {
    public UUIDUtil() {
    }

    public static String getUUIDWithoutConnector() {
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
    }
}