package com.supsi.alessandro.pollutionproblemclient.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.supsi.alessandro.pollutionproblemclient.PollutionApplication;
import com.supsi.alessandro.pollutionproblemclient.R;
import com.supsi.alessandro.pollutionproblemclient.activities.PollutionMapActivity;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;

import java.util.List;

/**
 * Created by Alessandro on 11/07/2017.
 */

public class HotCleanSpotsAdapter extends RecyclerView.Adapter<HotCleanSpotsAdapter.EventViewHolder> {

    private static final String TAG = HotCleanSpotsAdapter.class.getSimpleName();
    private final Activity mActivity;

    private List<Event> mDataSet;

    public HotCleanSpotsAdapter(Activity activity, List<Event> dataSet) {
        this.mActivity = activity;
        this.mDataSet = dataSet;
    }

    @Override
    public HotCleanSpotsAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card_view_layout, parent, false);
        return new EventViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HotCleanSpotsAdapter.EventViewHolder holder, int position) {
        //Update the cards values
        holder.text.setText(mDataSet.get(position).getPollutionValue());
        holder.mapView.setTag(mDataSet.get(position));

        // Ensure the map has been initialised by the on map ready callback in ViewHolder.
        // If it is not ready yet, it will be initialised with the NamedLocation set as its tag
        // when the callback is received.
        if (holder.map != null) {
            // The map is already ready to be used
            addMarker(holder.map, mDataSet.get(position));
        }
        holder.mapView.onResume();
    }

    private static void addMarker(GoogleMap map, Event e) {
        Log.d(TAG, "addMarker: ");

        LatLng gpsPosition = new LatLng(e.getDoubleGpsLat(), e.getDoubleGpsLong());
        float pollValue = Float.parseFloat(e.getPollutionValue());

        // Add a marker for this item and set the camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(gpsPosition, 15f));
        map.addMarker(new MarkerOptions().position(gpsPosition)
                .icon(BitmapDescriptorFactory.defaultMarker(PollutionMapActivity.getColor(pollValue)))
        );

        // Set the map type back to normal.
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     *
     */
    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

        MapView mapView;
        TextView text;
        GoogleMap map;

        public EventViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.tv_child_description);
            mapView = (MapView) itemView.findViewById(R.id.map_view);
            initializeMapView();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: TOUCHED ELEMENT " + getAdapterPosition());
            Event e = mDataSet.get(getAdapterPosition());
            LatLng position = new LatLng(e.getDoubleGpsLat(), e.getDoubleGpsLong());
            PollutionMapActivity.startWithInitialLocation(mActivity, position);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(PollutionApplication.getAppContext());
            map = googleMap;
            map.getUiSettings().setMapToolbarEnabled(false);
            map.setOnMapClickListener(this);
            Event event = (Event) mapView.getTag();
            if (event != null) {
                addMarker(map,event );
            }
        }


        /**
         * Initialises the MapView by calling its lifecycle methods.
         */
        public void initializeMapView() {
            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }
        }

        @Override
        public void onMapClick(LatLng latLng) {

        }
    }


}
