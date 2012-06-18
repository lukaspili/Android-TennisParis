package com.siu.android.tennisparis.util;

import android.util.Log;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public final class UrlUtils {

    private UrlUtils() {
    }

    public static URL getUrl(String urlAsString) {
        URL url;
        try {
            url = new URL(urlAsString);
        } catch (MalformedURLException e) {
            Log.w(UrlUtils.class.getName(), "Invalid format for url : " + urlAsString, e);
            return null;
        }

        return url;
    }

    public static String downloadData(String urlAsString) {
        URL url = getUrl(urlAsString);

        if (null == url) {
            Log.e(UrlUtils.class.getName(), "Invalid URL : " + urlAsString);
            return null;
        }

        InputStream is = null;
        String data;

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(15 * 1000);

            Log.d(UrlUtils.class.getName(), "Connection opened to : " + urlAsString);
            long time = System.currentTimeMillis();

            urlConnection.connect();
            is = urlConnection.getInputStream();
            data = IOUtils.toString(is);

            Log.d(UrlUtils.class.getName(), "Content downloaded in " + (System.currentTimeMillis() - time) + " ms");

        } catch (IOException e) {
            Log.e(UrlUtils.class.getName(), "Error during reading connection stream : " + e.getMessage());
            return null;

        } finally {
            IOUtils.closeQuietly(is);
        }

        return data;
    }
}
