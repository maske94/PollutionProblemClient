package com.supsi.alessandro.pollutionproblemclient;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alessandro on 01/05/2017.
 *
 */

public class PollutionApplication extends Application {
    private static Context mApplicationContext;

    public void onCreate() {
        super.onCreate();
        mApplicationContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mApplicationContext;
    }

}
