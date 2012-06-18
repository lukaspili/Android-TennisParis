package com.siu.android.tennisparis.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import com.siu.android.tennisparis.Application;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.app.activity.TennisDetailActivity;
import com.siu.android.tennisparis.dao.model.Availability;
import com.siu.android.tennisparis.dao.model.AvailabilityDao;
import com.siu.android.tennisparis.dao.model.Tennis;
import com.siu.android.tennisparis.database.DatabaseHelper;
import com.siu.android.tennisparis.json.GsonFormatter;
import com.siu.android.tennisparis.util.NetworkUtils;
import com.siu.android.tennisparis.util.StringUtils;
import com.siu.android.tennisparis.util.UrlUtils;
import org.apache.commons.codec2.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AvailabilityLoadTask extends AsyncTask<Void, Void, List<List<Availability>>> {

    private TennisDetailActivity activity;
    private SharedPreferences sharedPreferences;
    private Tennis tennis;

    public AvailabilityLoadTask(TennisDetailActivity activity, Tennis tennis) {
        this.activity = activity;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        this.tennis = tennis;
    }

    @Override
    protected List<List<Availability>> doInBackground(Void... voids) {
        Log.d(getClass().getName(), "AvailabilityLoadTask");

        List<Availability> availabilities;

        if (!NetworkUtils.isOnline()) {
            availabilities = getLocal();
        } else {
            availabilities = getWeb();
        }

        return filterByDay(availabilities);
    }

    private List<Availability> getLocal() {
        return DatabaseHelper.getInstance().getDaoSession().getAvailabilityDao()
                .queryBuilder()
                .where(AvailabilityDao.Properties.WebserviceTennisId.eq(tennis.getWebserviceId()))
                .orderAsc(AvailabilityDao.Properties.Day)
                .list();
    }

    private List<Availability> getWeb() {
        String data = UrlUtils.downloadData(Application.getContext().getString(R.string.webservice_availabilites_url));

        if (StringUtils.isEmpty(data)) {
            return getLocal();
        }

        // data successfully downloaded, compare md5
        final String currentMd5 = DigestUtils.md5Hex(data);
        final String existingMd5 = sharedPreferences.getString(Application.getContext().getString(R.string.application_preferences_availabilities_md5), "");

        Log.d(getClass().getName(), "Availability current md5 = " + currentMd5);
        Log.d(getClass().getName(), "Availability existing md5 = " + existingMd5);

        if (existingMd5.equals(currentMd5)) {
            return getLocal();
        }

        // data downloaded is different than local data, update local
        final List<Availability> availabilitiesFromWebservice = GsonFormatter.getGson().fromJson(data, new TypeToken<List<Availability>>() {
        }.getType());

        DatabaseHelper.getInstance().getDaoSession().runInTx(new Runnable() {
            @Override
            public void run() {
                AvailabilityDao availabilityDao = DatabaseHelper.getInstance().getDaoSession().getAvailabilityDao();
                availabilityDao.deleteAll();
                for (Iterator<Availability> it = availabilitiesFromWebservice.iterator(); it.hasNext(); ) {
                    availabilityDao.insert(it.next());
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Application.getContext().getString(R.string.application_preferences_availabilities_md5), currentMd5);
                editor.commit();
            }
        });

        List<Availability> availabilities = new ArrayList<Availability>();
        for (Iterator<Availability> it = availabilitiesFromWebservice.iterator(); it.hasNext(); ) {
            Availability availability = it.next();
            if (tennis.getWebserviceId().equals(availability.getWebserviceTennisId())) {
                availabilities.add(availability);
            }
        }

        return availabilities;
    }

    private List<List<Availability>> filterByDay(List<Availability> availabilities) {
        List<List<Availability>> availabilitiesPerDay = new ArrayList<List<Availability>>();

        if (availabilities.isEmpty()) {
            return availabilitiesPerDay;
        }

        availabilitiesPerDay.add(new ArrayList<Availability>());

        for (Iterator<Availability> it = availabilities.iterator(); it.hasNext(); ) {
            Availability availability = it.next();
            List<Availability> currentList = availabilitiesPerDay.get(availabilitiesPerDay.size() - 1);

            if (currentList.isEmpty()) {
                currentList.add(availability);
            } else {
                if (currentList.get(currentList.size() - 1).getDay().equals(availability.getDay())) {
                    currentList.add(availability);
                } else {
                    currentList = new ArrayList<Availability>();
                    currentList.add(availability);
                    availabilitiesPerDay.add(currentList);
                }
            }
        }

        return availabilitiesPerDay;
    }

    @Override
    protected void onPostExecute(List<List<Availability>> lists) {
        activity.onAvailabilityLoadTaskFinish(lists);
    }

    public void setActivity(TennisDetailActivity activity) {
        this.activity = activity;
    }
}
