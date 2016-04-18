package com.robertoallende.twitterclient.models;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.robertoallende.twitterclient.TwitterApplication;
import com.robertoallende.twitterclient.dao.DaoMaster;
import com.robertoallende.twitterclient.dao.DaoSession;

public class DbHelper {
    private static DbHelper instance;
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private SQLiteDatabase db;

    public synchronized static DbHelper getInstance() {
        if(instance == null) {
            instance = new DbHelper();
        }
        return instance;
    }

    public DbHelper() {
        Context appContext = TwitterApplication.getInstance().getApplicationContext();
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(appContext, "twitter", null);
        db = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}
