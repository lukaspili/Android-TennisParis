//package com.siu.android.tennisparis.service;
//
//import android.util.Log;
//import com.google.gson.reflect.TypeToken;
//import com.siu.android.tennisparis.Application;
//import com.siu.android.tennisparis.R;
//import com.siu.android.tennisparis.dao.model.Tennis;
//import com.siu.android.tennisparis.json.GsonFormatter;
//import com.siu.android.tennisparis.util.UrlUtils;
//import org.apache.commons.io.IOUtils;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//
///**
// * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
// */
//public class TennisService {
//
//    public List<Tennis> getTennisesFromWebservice() {
//        String urlAsString = Application.getContext().getString(R.string.webservice_tennis_url);
//        URL url = UrlUtils.getUrl(urlAsString);
//
//        if (null == url) {
//            Log.e(getClass().getName(), "Invalid URL : " + urlAsString);
//            return null;
//        }
//
//        List<Tennis> tennises;
//        BufferedReader bufferedReader = null;
//
//        try {
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setUseCaches(false);
//            urlConnection.setConnectTimeout(15 * 1000);
//
//            Log.d(getClass().getName(), "Connection opened to : " + urlAsString);
//            long time = System.currentTimeMillis();
//
//            urlConnection.connect();
//            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//            tennises = GsonFormatter.getGson().fromJson(bufferedReader, new TypeToken<List<Tennis>>() {}.getType());
//
//            Log.d(getClass().getName(), "Content downloaded and parsed in " + (System.currentTimeMillis() - time) + " ms");
//
//        } catch (IOException e) {
//            Log.w(getClass().getName(), "Error during reading connection stream : " + e.getMessage());
//            return null;
//
//        } finally {
//            IOUtils.closeQuietly(bufferedReader);
//        }
//
//        return tennises;
//    }
//
//    public List<Tennis> getTennisFromDatabase() {
//        return null;
//    }
//}
