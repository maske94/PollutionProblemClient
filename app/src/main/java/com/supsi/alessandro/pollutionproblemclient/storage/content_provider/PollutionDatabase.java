package com.supsi.alessandro.pollutionproblemclient.storage.content_provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alessandro on 01/06/2017.
 * <p>
 * Contains constants to manage the pollution database structure.
 */

class PollutionDatabase extends SQLiteOpenHelper {

    /**
     * Database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Filename for SQLite file.
     */
    private static final String DATABASE_NAME = "pollution.db";

    /**
     * Types declaration
     */
    //TODO define all the types for each column  es: COLUMN_TYPE_TIMESTAMP
    private static final String TYPE_STRING = " TEXT";
    private static final String TYPE_FLOAT = " REAL";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String COMMA_SEPARATOR = ",";

    /**
     * Sql statement to create the "events" table
     */
    private static final String TABLE_EVENTS_CREATE_QUERY =
            "CREATE TABLE " + PollutionContract.Event.TABLE_NAME + " (" +
                    PollutionContract.Event._ID + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEPARATOR +
                    PollutionContract.Event.COLUMN_NAME_TIMESTAMP + TYPE_STRING + COMMA_SEPARATOR +
                    PollutionContract.Event.COLUMN_NAME_USERNAME + TYPE_STRING + COMMA_SEPARATOR +
                    PollutionContract.Event.COLUMN_NAME_GPS_LAT + TYPE_STRING + COMMA_SEPARATOR +
                    PollutionContract.Event.COLUMN_NAME_GPS_LONG + TYPE_STRING + COMMA_SEPARATOR +
                    PollutionContract.Event.COLUMN_NAME_POLL_VALUE + TYPE_STRING + COMMA_SEPARATOR +
                    PollutionContract.Event.COLUMN_NAME_SYNCED + TYPE_INTEGER + COMMA_SEPARATOR +
                    PollutionContract.Event.COLUMN_NAME_CHILD_ID + TYPE_STRING + ")";

    /**
     * Sql statement to drop "events" table.
     */
    private static final String TABLE_EVENTS_DROP_QUERY =
            "DROP TABLE IF EXISTS " + PollutionContract.Event.TABLE_NAME;

    /**
     * Constructor
     *
     * @param context The application context
     */
    public PollutionDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_EVENTS_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // We want to persist existing data even if the version is upgraded, so do nothing here.
    }
}
