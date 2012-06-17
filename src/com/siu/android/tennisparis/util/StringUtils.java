package com.siu.android.tennisparis.util;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public final class StringUtils {

    public static boolean isEmpty(String value) {
        if (null == value || value.equals("")) {
            return true;
        }

        return false;
    }

    public static boolean isOneEmpty(String... values) {
        for (String value : values) {
            if (StringUtils.isEmpty(value)) {
                return true;
            }
        }

        return false;
    }
}
