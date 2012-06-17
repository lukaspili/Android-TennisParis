package com.siu.android.tennisparis.task;

import android.os.AsyncTask;
import android.util.Log;
import com.siu.android.tennisparis.app.activity.TennisMapActivity;
import com.siu.android.tennisparis.dao.model.Tennis;
import com.siu.android.tennisparis.dao.model.TennisDao;
import com.siu.android.tennisparis.database.DatabaseHelper;

import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class TennisLoadTask extends AsyncTask<Double, Void, List<Tennis>> {

    private TennisMapActivity activity;

    public TennisLoadTask(TennisMapActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Tennis> doInBackground(Double... coords) {
        Log.d(getClass().getName(), "TennisLoadTask");
        return DatabaseHelper.getInstance().getDaoSession().getTennisDao()
                .queryBuilder()
                .orderAsc(TennisDao.Properties.PostalCode)
                .list();
    }

    @Override
    protected void onPostExecute(List<Tennis> tennises) {
        activity.onTennisLoadTaskFinish(tennises);
    }

    public void setActivity(TennisMapActivity activity) {
        this.activity = activity;
    }
}
