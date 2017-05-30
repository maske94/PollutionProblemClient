package com.supsi.alessandro.pollutionproblemclient.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.supsi.alessandro.pollutionproblemclient.PollutionApplication;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;

import java.util.ArrayList;

/**
 * Created by Alessandro on 28/05/2017.
 */

public class PollutionSharedPreferences {

    private static final String TAG = PollutionSharedPreferences.class.getSimpleName();
    private static final PollutionSharedPreferences instance = new PollutionSharedPreferences();
    private static final String POLLUTION_SHARED_PREFERENCES_FILE_KEY = "com.supsi.alessandro.pollutionproblemclient.POLLUTION_APP_FILE_KEY";

    // Pollution shared preferences keys
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String CHILDREN_KEY = "children";
    private static final String CHILDREN_NOT_STORED_KEY = "childrenNotStored";
    private static final String USERNAME_NOT_STORED_KEY = "usernameNotStored";
    private static final String PASSWORD_NOT_STORED_KEY = "passwordNotStored";

    private SharedPreferences mSharedPreferences=null;


    /**
     * Private constructor in order to implement singleton pattern
     */
    private PollutionSharedPreferences() {
        Context context = PollutionApplication.getAppContext();
        mSharedPreferences = context.getSharedPreferences(POLLUTION_SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    /**
     * @return A singleton instance of this class
     */
    public static PollutionSharedPreferences getInstance() {
        return instance;
    }

    /**
     * Instantiates a shared preference instance
     */
    public void init() {

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

        // Returns CHILDREN_NOT_STORED_KEY if CHILDREN_KEY does not exist
        String storedString = mSharedPreferences.getString(CHILDREN_KEY, CHILDREN_NOT_STORED_KEY);

        /**
         *  Check whether the list of children already exist or not
         */

        // If does not exist
        if (storedString.equals(CHILDREN_NOT_STORED_KEY)) {

            ArrayList<Child> childrenList = new ArrayList<>();
            childrenList.add(child);

            String childrenListAsJson = gson.toJson(childrenList);
            editor.putString(CHILDREN_KEY, childrenListAsJson);
        }

        // If it already exists
        else {

            /**
             * Generate children array list from the json format.
             * Add the new child to the array list.
             * Finally, stored the new array list.
             */

            ArrayList<Child> childrenList = buildChildrenListFromJson(storedString);
            childrenList.add(child);

            // Restore the new children list with the new added child
            String childrenJson = gson.toJson(childrenList);
            editor.putString(CHILDREN_KEY, childrenJson);
        }

        editor.commit();
    }


    /**
     * Remove all the shared preferences of the logged in user
     */
    public void removeUser() {

        if (mSharedPreferences.contains(USERNAME_KEY)) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.remove(USERNAME_KEY)
                    .remove(PASSWORD_KEY)
                    .remove(CHILDREN_KEY);
            editor.commit();
        }
    }

    /**
     * Remove a child from the children list stored in shared preferences.
     *
     * @param child The child to remove
     */
    public void removeChild(@NonNull final Child child) {

        if (mSharedPreferences.contains(CHILDREN_KEY)) {
            ArrayList<Child> childrenList = buildChildrenListFromJson(mSharedPreferences.getString(CHILDREN_KEY, CHILDREN_NOT_STORED_KEY));
            boolean removed = childrenList.remove(child);

            if (removed) {
                // Store the new children list without the removed child
                String childrenJson = new Gson().toJson(childrenList);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(CHILDREN_KEY, childrenJson);
                editor.commit();
                Log.d(TAG, "removeChild() --> child removed");
            } else {
                Log.e(TAG, "removeChild() --> child NOT removed because it does not exist in the children list");
            }

        } else {
            Log.e(TAG, "removeChild() ---> " + CHILDREN_KEY + " key does not exist in shared preferences.");
        }
    }

    /**
     * Returns the stored username if it exists
     *
     * @return The stored username if it exits, if not returns a not stored username key string
     */
    public String getStoredUsername(){
        return mSharedPreferences.getString(USERNAME_KEY, USERNAME_NOT_STORED_KEY);
    }

    /**
     * Returns the stored password if it exists
     *
     * @return The stored password if it exits, if not returns a not stored password key string
     */
    public String getStoredPassword(){
        return mSharedPreferences.getString(PASSWORD_KEY, PASSWORD_NOT_STORED_KEY);
    }

    /**
     * Build the children list from the stored json shared preferences and then returns it.
     *
     * @return An array list of children if a children list has been previously stored, otherwise null.
     */
    public ArrayList<Child> getStoredChildrenList(){
        String jsonChildrenList = mSharedPreferences.getString(CHILDREN_KEY,CHILDREN_NOT_STORED_KEY);

        if(jsonChildrenList.equals(CHILDREN_NOT_STORED_KEY)){
            return null;
        }

        return buildChildrenListFromJson(jsonChildrenList);
    }

    /**
     * Transforms a json formatted string into an array list of child
     *
     * @param storedString The Json formatted string to be transformed in a Children Array List
     * @return The created children array list
     */
    private ArrayList<Child> buildChildrenListFromJson(String storedString) {
        Gson gson = new Gson();
        // workaround TypeToken<ArrayList<Child>>(){} --> creates an anonymous inner class that allows to call the getType() method,
        // which otherwise has private access in the TypeToken class.
        return gson.fromJson(storedString, new TypeToken<ArrayList<Child>>() {
        }.getType());
    }

}
