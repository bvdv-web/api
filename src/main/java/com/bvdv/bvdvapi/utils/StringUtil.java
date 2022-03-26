package com.bvdv.bvdvapi.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtil {
    public static String randomSessionId() {
        return RandomStringUtils.random(10, true, false);
    }
}
