package com.siu.android.tennisparis.database;

import com.siu.android.tennisparis.Application;
import com.siu.android.tennisparis.dao.model.DaoMaster;
import com.siu.android.tennisparis.dao.model.DaoSession;

public class DatabaseHelper {

    public static final String DB_NAME = "tennisparis.db";

    private static DatabaseHelper instance;

    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DatabaseHelper(){
    }

    public static DatabaseHelper getInstance() {
        if (null == instance) {
            instance = new DatabaseHelper();
        }

        return instance;
    }

    public DaoMaster getDaoMaster() {
        if (null == daoMaster) {
            daoMaster = new DaoMaster(new DaoMaster.DevOpenHelper(Application.getContext(), DB_NAME, null).getWritableDatabase());
        }

        return daoMaster;
    }

    public DaoSession getDaoSession() {
        if (null == daoSession) {
            daoSession = getDaoMaster().newSession();
        }

        return daoSession;
    }

}
