package com.supsi.alessandro.pollutionproblemclient.storage.content_provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Alessandro on 01/06/2017.
 *
 * Defines the local pollution database structure.
 * Sets tables and columns names as constants.
 */

public class PollutionContract  {

    /**
     * Private constructor
     */
    private PollutionContract(){

    }

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.supsi.alessandro.pollutionproblemclient";

    /**
     * Base URI.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path that identify the pollution events' table
     */
    public static final String PATH_EVENTS = "events";


    /**
     * Class that contains all that regards the table "events"
     */
    public static class Event implements BaseColumns{

        /**
         * MIME type for lists of events.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.pollutionproblemclient.events";

        /**
         * MIME type for individual events.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.pollutionproblemclient.event";

        /**
         * Fully qualified URI for "event" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENTS).build();

        /**
         * Table name where records are stored as "event" resources.
         */
        public static final String TABLE_NAME = "events";

        /**
         * Column timestamp: Timestamp when the event has been detected.
         */
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";

        /**
         * Column child_id: The child to which the event is associated.
         */
        public static final String COLUMN_NAME_CHILD_ID = "child_id";

        /**
         * Column poll_value: The pollution value detected for the event.
         */
        public static final String COLUMN_NAME_POLL_VALUE = "poll_value";

        /**
         * Column gps_lat: The gps latitude where the event has been detected.
         */
        public static final String COLUMN_NAME_GPS_LAT = "gps_lat";

        /**
         * Column gps_long: The gps longitude where the event has been detected.
         */
        public static final String COLUMN_NAME_GPS_LONG = "gps_long";

    }

}
