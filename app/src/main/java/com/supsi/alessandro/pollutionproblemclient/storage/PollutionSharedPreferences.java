package com.supsi.alessandro.pollutionproblemclient.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.supsi.alessandro.pollutionproblemclient.PollutionApplication;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;

import java.util.ArrayList;

/**
 * Created by Alessandro on 28/05/2017.
 */

public class PollutionSharedPreferences {

    private static final PollutionSharedPreferences instance = new PollutionSharedPreferences();
    private static final String POLLUTION_SHARED_PREFERENCES_FILE_KEY = "com.supsi.alessandro.pollutionproblemclient.POLLUTION_APP_FILE_KEY";
    private SharedPreferences mSharedPreferences;

    // Pollution shared preferences keys
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String CHILDREN_KEY = "children";
    private static final String CHILDREN_NOT_EXIST_KEY = "childrenNotExists";

    /**
     * Private constructor in order to implement singleton pattern
     */
    private PollutionSharedPreferences() {

    }

    /**
     * @return A singleton instance of this class
     */
    static PollutionSharedPreferences getInstance() {
        return instance;
    }

    /**
     * Instantiates a shared preference instance
     */
    public void init() {
        Context context = PollutionApplication.getAppContext();
        mSharedPreferences = context.getSharedPreferences(POLLUTION_SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    /**
     * Stores username and password in the shared preferences
     *
     * @param username Logged in user's username
     * @param password Logged in user's password
     */
    public void storeUser(@NonNull final String username, @NonNull final String password) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USERNAME_KEY, username)
                .putString(PASSWORD_KEY, password);
        editor.commit();
    }

    /**
     * Checks if there's already stored a list of children in the shared preferences.
     * If so, adds the given child to the list.
     * If not, creates a new list and store the first given child.
     *
     * @param child The child be stored
     */
    public void storeChild(@NonNull final Child child) {

        Gson gson = new Gson();
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        // Returns CHILDREN_NOT_EXIST_KEY if CHILDREN_KEY does not exist
        String storedString = mSharedPreferences.getString(CHILDREN_KEY, CHILDREN_NOT_EXIST_KEY);

        /**
         *  Check whether the list of children already exist or not
         */

        // If does not exist
        if (storedString.equals(CHILDREN_NOT_EXIST_KEY)) {

            ArrayList<Child> childrenList = new ArrayList<>();
            childrenList.add(child);

            String childrenListAsJson = gson.toJson(childrenList);
            editor.putString(CHILDREN_KEY, childrenListAsJson);
        }

        // If it already exist
        else {

            /**
             * Generate children array list from the json format.
             * Add the new child to the array list.
             * Finally, stored the new array list.
             */

            // workaround TypeToken<ArrayList<Child>>(){} --> creates an anonymous inner class that allows to call the getType() method,
            // which otherwise has private access in the TypeToken class.
            ArrayList<Child> childrenList = gson.fromJson(storedString, new TypeToken<ArrayList<Child>>() {
            }.getType());
            childrenList.add(child);

            // Restore the new children list with the new added child
            String childrenJson = gson.toJson(storedString);
            editor.putString(CHILDREN_KEY, childrenJson);
        }

        editor.commit();
    }




}
