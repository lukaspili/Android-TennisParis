//package com.siu.android.tennisparis.task;
//
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import com.google.gson.reflect.TypeToken;
//import com.siu.android.tennisparis.Application;
//import com.siu.android.tennisparis.R;
//import com.siu.android.tennisparis.app.activity.TennisMapActivity;
//import com.siu.android.tennisparis.dao.model.Availability;
//import com.siu.android.tennisparis.dao.model.AvailabilityDao;
//import com.siu.android.tennisparis.dao.model.Tennis;
//import com.siu.android.tennisparis.dao.model.TennisDao;
//import com.siu.android.tennisparis.database.DatabaseHelper;
//import com.siu.android.tennisparis.json.GsonFormatter;
//import com.siu.android.tennisparis.util.NetworkUtils;
//import com.siu.android.tennisparis.util.StringUtils;
//import com.siu.android.tennisparis.util.UrlUtils;
//import org.apache.commons.codec2.digest.DigestUtils;
//import org.apache.commons.io.IOUtils;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
// */
//public class TennisUpdateTask extends AsyncTask<Void, Void, Void> {
//
//    private TennisMapActivity activity;
//    private SharedPreferences sharedPreferences;
//    private boolean progessCalled;
//
//    public TennisUpdateTask(TennisMapActivity activity) {
//        this.activity = activity;
//        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//        Log.d(getClass().getName(), "TennisUpdateTask");
//
//        if (!NetworkUtils.isOnline()) {
//            return null;
//        }
//
//        // database is empty
//        if (!sharedPreferences.contains(Application.getContext().getString(R.string.application_preferences_tennis_md5))) {
//
//            // update tennis first
//            if (!updateTennises()) {
//                return null;
//            }
//
//            // and then show them directly for the map
//            publishProgress();
//
//            if (!updateAvailabilities()) {
//                return null;
//            }
//
//        } else {
//            // database is not empty, so logically avaibalities should be updated more often than tennises, so
//            // check them first
//            if (!updateAvailabilities()) {
//                return null;
//            }
//
//            if (!updateTennises()) {
//                return null;
//            }
//        }
//
//        return null;
//    }
//
//    private boolean updateAvailabilities() {
//        String data = getData(Application.getContext().getString(R.string.webservice_tennis_url));
//        if (StringUtils.isEmpty(data)) {
//            return false;
//        }
//
//        String currentMd5 = DigestUtils.md5Hex(data);
//        String existingMd5 = sharedPreferences.getString(Application.getContext().getString(R.string.application_preferences_availabilities_md5), "");
//
//        Log.d(getClass().getName(), "Availability current md5 = " + currentMd5);
//        Log.d(getClass().getName(), "Availability existing md5 = " + existingMd5);
//
//        if (existingMd5.equals(currentMd5)) {
//            return false;
//        }
//
//        final List<Availability> availabilities = GsonFormatter.getGson().fromJson(data, new TypeToken<List<Availability>>() {
//        }.getType());
//        if (null == availabilities || availabilities.isEmpty()) {
//            return false;
//        }
//
//        final AvailabilityDao availabilityDao = DatabaseHelper.getInstance().getDaoSession().getAvailabilityDao();
//        availabilityDao.getSession().runInTx(new Runnable() {
//            @Override
//            public void run() {
//                availabilityDao.deleteAll();
//                for (Iterator<Availability> it = availabilities.iterator(); it.hasNext(); ) {
//                    availabilityDao.insert(it.next());
//                }
//            }
//        });
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(Application.getContext().getString(R.string.application_preferences_availabilities_md5), currentMd5);
//        editor.commit();
//
//        return true;
//    }
//
//    private boolean updateTennises() {
//        String data = getData(Application.getContext().getString(R.string.webservice_tennis_url));
//        if (StringUtils.isEmpty(data)) {
//            return false;
//        }
//
//        String currentMd5 = DigestUtils.md5Hex(data);
//        String existingMd5 = sharedPreferences.getString(Application.getContext().getString(R.string.application_preferences_tennis_md5), "");
//
//        Log.d(getClass().getName(), "Tennis current md5 = " + currentMd5);
//        Log.d(getClass().getName(), "Tennis existing md5 = " + existingMd5);
//
//        if (existingMd5.equals(currentMd5)) {
//            return false;
//        }
//
//        final List<Tennis> tennises = GsonFormatter.getGson().fromJson(data, new TypeToken<List<Tennis>>() {
//        }.getType());
//        if (null == tennises || tennises.isEmpty()) {
//            return false;
//        }
//
//        final TennisDao tennisDao = DatabaseHelper.getInstance().getDaoSession().getTennisDao();
//        tennisDao.getSession().runInTx(new Runnable() {
//            @Override
//            public void run() {
//                tennisDao.deleteAll();
//                for (Iterator<Tennis> it = tennises.iterator(); it.hasNext(); ) {
//                    tennisDao.insert(it.next());
//                }
//            }
//        });
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(Application.getContext().getString(R.string.application_preferences_tennis_md5), currentMd5);
//        editor.commit();
//
//        return true;
//    }
//
//    private String getData(String urlAsString) {
//        URL url = UrlUtils.getUrl(urlAsString);
//
//        if (null == url) {
//            Log.e(getClass().getName(), "Invalid URL : " + urlAsString);
//            return null;
//        }
//
//        InputStream is = null;
//        String data;
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
//            is = urlConnection.getInputStream();
//            data = IOUtils.toString(is);
//
//            Log.d(getClass().getName(), "Content downloaded in " + (System.currentTimeMillis() - time) + " ms");
//
//        } catch (IOException e) {
//            Log.e(getClass().getName(), "Error during reading connection stream : " + e.getMessage());
//            return null;
//
//        } finally {
//            IOUtils.closeQuietly(is);
//        }
//
//        return data;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        activity.onTennisUpdateTaskFinish(progessCalled);
//    }
//
//    public void setActivity(TennisMapActivity activity) {
//        this.activity = activity;
//    }
//}