package com.siu.android.tennisparis.util;

import org.apache.commons.lang3.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class DateUtils {

    private static SimpleDateFormat simpleDateFormat;

    public static String formatAsFull(Date date) {
        return WordUtils.capitalize(getSimpleDateFormat().format(date));
    }

    private static SimpleDateFormat getSimpleDateFormat() {
        if (null == simpleDateFormat) {
            simpleDateFormat = new SimpleDateFormat("EEEEEEEE dd MMMMMM", Locale.getDefault());
        }

        return simpleDateFormat;
    }
}
