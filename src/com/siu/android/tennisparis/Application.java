package com.siu.android.tennisparis;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.siu.android.tennisparis.dao.model.Availability;
import com.siu.android.tennisparis.dao.model.Tennis;
import com.siu.android.tennisparis.database.DatabaseHelper;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class Application extends android.app.Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        if(getResources().getBoolean(R.bool.application_debug)) {
            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
            edit.remove(getString(R.string.application_preferences_tennis_md5));
            edit.remove(getString(R.string.application_preferences_availabilities_md5));
            edit.commit();

            DatabaseHelper.getInstance().getDaoSession().deleteAll(Availability.class);
            DatabaseHelper.getInstance().getDaoSession().deleteAll(Tennis.class);
        }
    }

    public static Context getContext() {
        return context;
    }
}