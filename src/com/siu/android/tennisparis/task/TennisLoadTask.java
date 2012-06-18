package com.siu.android.tennisparis.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import com.siu.android.tennisparis.Application;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.app.activity.TennisMapActivity;
import com.siu.android.tennisparis.dao.model.Tennis;
import com.siu.android.tennisparis.dao.model.TennisDao;
import com.siu.android.tennisparis.database.DatabaseHelper;
import com.siu.android.tennisparis.json.GsonFormatter;
import com.siu.android.tennisparis.util.NetworkUtils;
import com.siu.android.tennisparis.util.StringUtils;
import com.siu.android.tennisparis.util.UrlUtils;
import org.apache.commons.codec2.digest.DigestUtils;

import java.util.Iterator;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class TennisLoadTask extends AsyncTask<Void, Void, List<Tennis>> {

    private TennisMapActivity activity;
    private SharedPreferences sharedPreferences;

    public TennisLoadTask(TennisMapActivity activity) {
        this.activity = activity;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    protected List<Tennis> doInBackground(Void... unused) {
        Log.d(getClass().getName(), "TennisLoadTask");

        if (!NetworkUtils.isOnline()) {
            return getLocal();
        }

        return getWeb();
    }

    public List<Tennis> getLocal() {
        return DatabaseHelper.getInstance().getDaoSession().getTennisDao()
                .queryBuilder()
                .orderAsc(TennisDao.Properties.PostalCode)
                .list();
    }

    public List<Tennis> getWeb() {
        String data = UrlUtils.downloadData(Application.getContext().getString(R.string.webservice_tennis_url));
        if (StringUtils.isEmpty(data)) {
            return getLocal();
        }

        final String currentMd5 = DigestUtils.md5Hex(data);
        final String existingMd5 = sharedPreferences.getString(Application.getContext().getString(R.string.application_preferences_tennis_md5), "");

        Log.d(getClass().getName(), "Tennis current md5 = " + currentMd5);
        Log.d(getClass().getName(), "Tennis existing md5 = " + existingMd5);

        if (existingMd5.equals(currentMd5)) {
            return getLocal();
        }

        final List<Tennis> tennises = GsonFormatter.getGson().fromJson(data, new TypeToken<List<Tennis>>() {
        }.getType());

        DatabaseHelper.getInstance().getDaoSession().runInTx(new Runnable() {
            @Override
            public void run() {
                TennisDao tennisDao = DatabaseHelper.getInstance().getDaoSession().getTennisDao();
                tennisDao.deleteAll();
                for (Iterator<Tennis> it = tennises.iterator(); it.hasNext(); ) {
                    tennisDao.insert(it.next());
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Application.getContext().getString(R.string.application_preferences_tennis_md5), currentMd5);
                editor.commit();
            }
        });

        return tennises;
    }

    @Override
    protected void onPostExecute(List<Tennis> tennises) {
        activity.onTennisLoadTaskFinish(tennises);
    }

    public void setActivity(TennisMapActivity activity) {
        this.activity = activity;
    }
}
