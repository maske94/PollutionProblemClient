package com.supsi.alessandro.pollutionproblemclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.supsi.alessandro.pollutionproblemclient.PollutionApplication;
import com.supsi.alessandro.pollutionproblemclient.R;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.storage.content_provider.PollutionProvider;

import java.util.ArrayList;

/**
 * Created by Alessandro on 22/06/2017.
 */

public class PollutionMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = PollutionMapActivity.class.getSimpleName();
    private static final String INTENT_INITIAL_LOCATION = "initialLocation";

    private LatLng mInitialLocation=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);

        /**
         * Check if this activity has been started with some parameters
         * to define the initial position of the camera.
         */
        Intent i = getIntent();
        if(i != null && i.hasExtra(INTENT_INITIAL_LOCATION)) {
            Log.d(TAG, "onCreate: GOT INITIAL POSITION;");
            mInitialLocation = i.getParcelableExtra(INTENT_INITIAL_LOCATION);
        }

        // Test: add some events to db
//        try {
//            addEvents();
//        } catch (RemoteException | OperationApplicationException e) {
//            e.printStackTrace();
//        }

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void addEvents() throws RemoteException, OperationApplicationException {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("maske94", "prova", "10", "2017-06-22T22:03:00", "46.0085418", "8.951052000000004"));
        events.add(new Event("maske94", "prova", "20", "2017-06-22T22:03:00", "46.0185418", "8.951052000000004"));
        events.add(new Event("maske94", "prova", "40", "2017-06-22T22:03:00", "46.0285418", "8.951052000000004"));
        events.add(new Event("maske94", "prova", "70", "2017-06-22T22:03:00", "46.0385418", "8.951052000000004"));
        events.add(new Event("maske94", "prova", "200", "2017-06-22T22:03:00", "46.0485418", "8.951052000000004"));
        events.add(new Event("maske94", "prova", "300", "2017-06-22T22:03:00", "46.0585418", "8.951052000000004"));
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
            googleMap.addMarker(new MarkerOptions().position(eventPosition)
                    .title(e.getPollutionValue())
                    .icon(BitmapDescriptorFactory.defaultMarker(getColor(Float.parseFloat(e.getPollutionValue())))));
        }

        LatLng position;
        if(mInitialLocation!=null){
            position = mInitialLocation;
        }else {
            position = new LatLng(46.0036778d, 8.951052000000004d);// Lugano
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * Computes the color of the marker depending on
     * the pollution value.
     * The way that is used to choose the color
     * is based on the standard PM2.5 AQI scale.
     * {@see http://aqicn.org/faq/2013-09-09/revised-pm25-aqi-breakpoints/}
     *
     * @param pollValue The given pollution value.
     * @return A float representing the color on a color wheel.
     */
    public static float getColor(float pollValue){
        if(pollValue <= 12.0f){
            Log.d(TAG, "getColor: Good");
            return BitmapDescriptorFactory.HUE_GREEN;
        }else if(pollValue <= 35.4f){
            Log.d(TAG, "getColor: Moderate");
            return BitmapDescriptorFactory.HUE_YELLOW;
        }else if(pollValue <= 55.4f){
            Log.d(TAG, "getColor: Unhealthy for Sensitive Groups");
            return BitmapDescriptorFactory.HUE_ORANGE;
        }else if(pollValue <= 150.4f){
            Log.d(TAG, "getColor: Unhealthy");
            return BitmapDescriptorFactory.HUE_RED;
        }else if(pollValue <= 250.4f){
            Log.d(TAG, "getColor: Very Unhealthy");
            return BitmapDescriptorFactory.HUE_VIOLET;
        }else{
            Log.d(TAG, "getColor: Hazardous");
            return BitmapDescriptorFactory.HUE_VIOLET-20;
        }
    }

    /**
     * Start this activity providing it with a initial location.
     *
     * @param mActivity Activity from where start the new one.
     * @param initialLocation The initial location where to locate the camera.
     */
    public static void startWithInitialLocation(Activity mActivity, LatLng initialLocation){
        Intent i = new Intent(mActivity,PollutionMapActivity.class);
        i.putExtra(INTENT_INITIAL_LOCATION,initialLocation);
        mActivity.startActivity(i);
    }

}

