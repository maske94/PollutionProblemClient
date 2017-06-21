package com.supsi.alessandro.pollutionproblemclient;

import android.app.Application;
import android.content.Context;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.storage.PollutionSharedPreferences;

import java.util.ArrayList;

/**
 * Created by Alessandro on 01/05/2017.
 */

public class PollutionApplication extends Application {
    private static Context mApplicationContext;
    private static PollutionSharedPreferences mSharedPreferences;

    public void onCreate() {
        super.onCreate();
        mApplicationContext = getApplicationContext();
        mSharedPreferences = PollutionSharedPreferences.getInstance();
    }

    /**
     * Returns a static reference to the application context.
     *
     * @return The application context
     */
    public static Context getAppContext() {
        return mApplicationContext;
    }

    /**
     * Returns the username of the logged user, by harnessing the {@link PollutionSharedPreferences} class.
     *
     * @return The username is there is a logged in user, null otherwise
     */
    public static String getLoggedUsername() {
        return mSharedPreferences.getStoredUsername();
    }

    /**
     * Loops over each child stored in shared preferences for the current username
     * and return the childId associated with the given deviceId.
     *
     * @param deviceId The id of the device associated to the childId that has to be returned
     * @return The matched childId if it exists, null otherwise.
     */
    public static String getChildId(String deviceId) {
        ArrayList<Child> children = mSharedPreferences.getStoredChildrenList();

        if(children==null){
            return null;
        }

        for (Child c : children) {
            if (c.getDeviceId().equals(deviceId)) {
                return c.getChildId();

            }
        }

        return null;
    }
}
