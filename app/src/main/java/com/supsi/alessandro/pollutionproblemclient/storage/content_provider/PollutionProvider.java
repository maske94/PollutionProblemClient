package com.supsi.alessandro.pollutionproblemclient.storage.content_provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Alessandro on 05/06/2017.
 */

public class PollutionProvider extends ContentProvider {

    private static final String TAG = PollutionProvider.class.getSimpleName();

    /**
     * Reference to the pollution db helper,
     * necessary to get readable and writable database on which perform queries
     */
    private PollutionDatabase mPollutionDatabase;

    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = PollutionContract.CONTENT_AUTHORITY;

    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using mUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through mUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.

    /**
     * URI ID for route: /events
     */
    public static final int ROUTE_EVENTS = 1;

    /**
     * It's a best practice to provide a route to query a single
     * row in a table by id.
     * URI ID for route: /events/{ID}
     */
    public static final int ROUTE_EVENTS_ID = 2;

    /**
     * Default values for the events
     */
    static final int EVENT_SYNCED = 1;
    static final int EVENT_NOT_SYNCED = 0;

    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(AUTHORITY, PollutionContract.PATH_EVENTS, ROUTE_EVENTS);
        mUriMatcher.addURI(AUTHORITY, PollutionContract.PATH_EVENTS_ID, ROUTE_EVENTS_ID);
    }


    @Override
    public boolean onCreate() {
        mPollutionDatabase = new PollutionDatabase(getContext());
        return true;
    }

    /**
     * Determine the mime type for entries returned by a given URI.
     *
     * @param uri The Uri to match with return type
     * @return The type of the given URI
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case ROUTE_EVENTS:
                return PollutionContract.Event.CONTENT_TYPE;
            case ROUTE_EVENTS_ID:
                return PollutionContract.Event.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Perform a selection query on the database.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mPollutionDatabase.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = mUriMatcher.match(uri);

        switch (uriMatch) {
            case ROUTE_EVENTS_ID:
                // Return a single entry, by ID.
                String id = uri.getLastPathSegment();
                builder.where(PollutionContract.Event._ID + "=?", id);

            case ROUTE_EVENTS:
                // Return all known entries.
                builder.table(PollutionContract.Event.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);

                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Insert a new entry into the database.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mPollutionDatabase.getWritableDatabase();
        assert db != null;

        final int match = mUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_EVENTS:
                long id = db.insertOrThrow(PollutionContract.Event.TABLE_NAME, null, contentValues);
                if (id == -1) {
                    Log.e(TAG, "insert: ERROR WHEN INSERTING IN DB");
                }
                result = Uri.parse(PollutionContract.Event.CONTENT_URI + "/" + id);
                break;
            case ROUTE_EVENTS_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    /**
     * Delete rows from the db.
     *
     * @return The number of deleted rows
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mPollutionDatabase.getWritableDatabase();
        assert db != null;

        final int match = mUriMatcher.match(uri);
        int count;

        switch (match) {
            case ROUTE_EVENTS:
                count = builder.table(PollutionContract.Event.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_EVENTS_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(PollutionContract.Event.TABLE_NAME)
                        .where(PollutionContract.Event._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        //TODO implement update when events are sent to the server
        return 0;
    }

    /**
     * Stores multiple events by exploiting the batch mechanism.
     *
     * @param events          The list of events to be stored
     * @param contentResolver An instance of content resolver
     */
    public static ContentProviderResult[] storeEvents(ArrayList<Event> events, ContentResolver contentResolver) throws RemoteException, OperationApplicationException {

        ArrayList<ContentProviderOperation> batch = new ArrayList<>();

        for (Event e : events) {
            batch.add(ContentProviderOperation.newInsert(PollutionContract.Event.CONTENT_URI)
                    .withValue(PollutionContract.Event.COLUMN_NAME_USERNAME, e.getUsername())
                    .withValue(PollutionContract.Event.COLUMN_NAME_CHILD_ID, e.getChildId())
                    .withValue(PollutionContract.Event.COLUMN_NAME_GPS_LAT, e.getGpsLat())
                    .withValue(PollutionContract.Event.COLUMN_NAME_GPS_LONG, e.getGpsLong())
                    .withValue(PollutionContract.Event.COLUMN_NAME_POLL_VALUE, e.getPollutionValue())
                    .withValue(PollutionContract.Event.COLUMN_NAME_TIMESTAMP, e.getTimeStamp())
                    .withValue(PollutionContract.Event.COLUMN_NAME_SYNCED, EVENT_NOT_SYNCED)
                    .build());

        }

        ContentProviderResult[] results = contentResolver.applyBatch(PollutionContract.CONTENT_AUTHORITY, batch);

        Log.d(TAG, "storeEvents() ---> results after batch operation: " + Arrays.toString(results));

        return results;
    }

    /**
     * Deletes multiple events by exploiting the batch mechanism.
     *
     * @param eventsIds       The list of events to be dropped
     * @param contentResolver An instance of content resolver
     */
    public static ContentProviderResult[] deleteEvents(ArrayList<Integer> eventsIds, ContentResolver contentResolver) throws RemoteException, OperationApplicationException {

        ArrayList<ContentProviderOperation> batch = new ArrayList<>();
        Uri eventToDropUri;

        for (Integer i : eventsIds) {
            eventToDropUri = PollutionContract.Event.CONTENT_URI.buildUpon().appendPath(Integer.toString(i)).build();
            batch.add(ContentProviderOperation.newDelete(eventToDropUri)
                    .build());
        }

        ContentProviderResult[] results = contentResolver.applyBatch(PollutionContract.CONTENT_AUTHORITY, batch);

        Log.d(TAG, "deleteEvents() ---> results after batch operation: " + Arrays.toString(results));

        return results;
    }

    /**
     * Utility method to select events from the database by exploiting the content provider.
     * Prepares select statement by using the given attributes.
     * Executes the query method though the given contentResolver.
     *
     * @param username Username to which filter the events
     * @param childId child id to which filter the events
     * @param dateStart timestamp to which start filtering the events, can be null
     * @param dateEnd timestamp to which end filtering the events, can be null
     * @param contentResolver contentResolver used to interact with the content provider
     *
     * @return An array list of events that satisfy the given filters
     */
    public static ArrayList<Event> getEvents(String username, String childId, String dateStart, String dateEnd, ContentResolver contentResolver) {

        assert username != null;
        assert childId != null;
        assert contentResolver != null;

        /**
         * Build selection string and selectionArgs string depending on the given date bounds ( dateStart and dateEnd ).
         */
        String selection;
        String[] selectionArgs;

        if (dateStart == null && dateEnd == null) {// In this case we don't use any timestamp bound
            selection = PollutionContract.Event.COLUMN_NAME_USERNAME + "=? AND " + PollutionContract.Event.COLUMN_NAME_CHILD_ID + "=?";
            selectionArgs = new String[2];
            selectionArgs[0] = username;
            selectionArgs[1] = childId;
        } else if (dateEnd == null) {// It means that we have only the lower bound: dateStart
            selection = PollutionContract.Event.COLUMN_NAME_USERNAME + "=? AND " + PollutionContract.Event.COLUMN_NAME_CHILD_ID + "=? AND " + PollutionContract.Event.COLUMN_NAME_TIMESTAMP + ">=?";
            selectionArgs = new String[3];
            selectionArgs[0] = username;
            selectionArgs[1] = childId;
            selectionArgs[2] = dateStart;
        } else if (dateStart == null) {// It means that we have only the upper bound: dateEnd
            selection = PollutionContract.Event.COLUMN_NAME_USERNAME + "=? AND " + PollutionContract.Event.COLUMN_NAME_CHILD_ID + "=? AND " + PollutionContract.Event.COLUMN_NAME_TIMESTAMP + "<=?";
            selectionArgs = new String[3];
            selectionArgs[0] = username;
            selectionArgs[1] = childId;
            selectionArgs[2] = dateEnd;
        } else {// We have both bounds: dateStart and dateEnd
            selection = PollutionContract.Event.COLUMN_NAME_USERNAME + "=? AND " + PollutionContract.Event.COLUMN_NAME_CHILD_ID + "=? AND " + PollutionContract.Event.COLUMN_NAME_TIMESTAMP + ">=? AND " + PollutionContract.Event.COLUMN_NAME_TIMESTAMP + "<=?";
            selectionArgs = new String[4];
            selectionArgs[0] = username;
            selectionArgs[1] = childId;
            selectionArgs[2] = dateStart;
            selectionArgs[3] = dateEnd;
        }

        Cursor c = contentResolver.query(PollutionContract.Event.CONTENT_URI, PollutionContract.Event.COLUMNS_PROJECTION, selection, selectionArgs, null);
        assert c != null;

        ArrayList<Event> events = buildEventsFromCursor(c);
        c.close();
        Log.d(TAG, "getEvents() ---> selected events: "+events);
        return events;
    }

    /**
     * Transforms the result coming from a {@link Cursor} into an {@link ArrayList} of events.
     *
     * @param c The {@link Cursor} to be transformed into an {@link ArrayList} of events.
     * @return The {@link ArrayList} of events
     */
    private static ArrayList<Event> buildEventsFromCursor(Cursor c) {
        ArrayList<Event> events = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(PollutionContract.Event._ID));
                String username = c.getString(c.getColumnIndex(PollutionContract.Event.COLUMN_NAME_USERNAME));
                String childId = c.getString(c.getColumnIndex(PollutionContract.Event.COLUMN_NAME_CHILD_ID));
                String timestamp = c.getString(c.getColumnIndex(PollutionContract.Event.COLUMN_NAME_TIMESTAMP));
                float pollValue = c.getFloat(c.getColumnIndex(PollutionContract.Event.COLUMN_NAME_POLL_VALUE));
                float gpsLat = c.getFloat(c.getColumnIndex(PollutionContract.Event.COLUMN_NAME_GPS_LAT));
                float gpsLong = c.getFloat(c.getColumnIndex(PollutionContract.Event.COLUMN_NAME_GPS_LONG));
                int synced = c.getInt(c.getColumnIndex(PollutionContract.Event.COLUMN_NAME_SYNCED));

                // Creates the new event
                Event e = new Event(username,childId,pollValue,timestamp,gpsLat,gpsLong);
                e.setEventId(id);
                e.setSynced(synced);

                events.add(e);
            } while (c.moveToNext());
        }

        return events;
    }

    /**
     * Remove all the data from the database.
     *
     * @param contentResolver contentResolver used to interact with the content provider
     * @return The number of deleted rows
     */
    public static int cleanAll(ContentResolver contentResolver) {
        return contentResolver.delete(PollutionContract.Event.CONTENT_URI,null,null);
    }
}
