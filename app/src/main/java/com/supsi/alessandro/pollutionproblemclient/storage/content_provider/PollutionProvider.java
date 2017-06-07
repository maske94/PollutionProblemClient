package com.supsi.alessandro.pollutionproblemclient.storage.content_provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Alessandro on 05/06/2017.
 */

public class PollutionProvider extends ContentProvider {

    private static final String TAG = PollutionProvider.class.getSimpleName();

    /**
     * Reference to the pollution db helper,
     * necessary to get readable and writable database on which perform queries
     */
    PollutionDatabase mPollutionDatabase;

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
                if(id==-1) {
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

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
