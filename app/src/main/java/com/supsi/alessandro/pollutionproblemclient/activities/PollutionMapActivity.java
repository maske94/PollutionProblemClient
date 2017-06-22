package com.supsi.alessandro.pollutionproblemclient.activities;

import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.supsi.alessandro.pollutionproblemclient.R;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.storage.content_provider.PollutionProvider;

import java.util.ArrayList;

/**
 * Created by Alessandro on 22/06/2017.
 */

public class PollutionMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = PollutionMapActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);

        // Test: add some events to db
//        try {
//            addEvents();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        } catch (OperationApplicationException e) {
//            e.printStackTrace();
//        }

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void addEvents() throws RemoteException, OperationApplicationException {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("maske94", "prova", "90", "2017-06-22T22:03:00", "46.1485418", "9.308567700000026"));
        events.add(new Event("maske94", "prova", "90", "2017-06-22T22:03:00", "46.2485418", "9.308567700000026"));
        events.add(new Event("maske94", "prova", "90", "2017-06-22T22:03:00", "46.3485418", "9.308567700000026"));
        events.add(new Event("maske94", "prova", "90", "2017-06-22T22:03:00", "46.4485418", "9.308567700000026"));
        PollutionProvider.storeEvents(events, getContentResolver());
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        /**
         * Load from content provider the events to be shown on the map
         */
        ArrayList<Event> events = PollutionProvider.getEvents("maske94", "prova", null, null, getContentResolver());

        if (events == null) {
            Log.e(TAG, "onMapReady() --> no EVENTS for the current username and child id");
            return;
        }

//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));

        /**
         * Add a new marker for each event previously loaded
         */
        for (Event e : events) {
            LatLng eventPosition = new LatLng(e.getDoubleGpsLat(), e.getDoubleGpsLong());
            googleMap.addMarker(new MarkerOptions().position(eventPosition));
        }

        LatLng gravedona = new LatLng(events.get(0).getDoubleGpsLat(),events.get(0).getDoubleGpsLat());

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(gravedona));
    }
}

