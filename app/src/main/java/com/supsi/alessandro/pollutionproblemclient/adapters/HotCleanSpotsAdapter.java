package com.supsi.alessandro.pollutionproblemclient.adapters;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.supsi.alessandro.pollutionproblemclient.PollutionApplication;
import com.supsi.alessandro.pollutionproblemclient.R;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;

import java.util.List;

/**
 * Created by Alessandro on 11/07/2017.
 */

public class HotCleanSpotsAdapter extends RecyclerView.Adapter<HotCleanSpotsAdapter.EventViewHolder> {

    private static final String TAG = HotCleanSpotsAdapter.class.getSimpleName();

    private List<Event> mDataSet;

    public HotCleanSpotsAdapter(List<Event> dataSet){
        this.mDataSet=dataSet;
    }

    @Override
    public HotCleanSpotsAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout, parent, false);
        return new EventViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HotCleanSpotsAdapter.EventViewHolder holder, int position) {
        //Update the cards values
        holder.text.setText(mDataSet.get(position).getPollutionValue());


        LatLng gpsPosition = new LatLng(mDataSet.get(position).getDoubleGpsLat(),mDataSet.get(position).getDoubleGpsLong());
        holder.mapView.setTag(gpsPosition);

        // Ensure the map has been initialised by the on map ready callback in ViewHolder.
        // If it is not ready yet, it will be initialised with the NamedLocation set as its tag
        // when the callback is received.
        if (holder.map != null) {
            // The map is already ready to be used
            setMapLocation(holder.map, gpsPosition);
        }
        holder.mapView.onResume();
    }

    private static void setMapLocation(GoogleMap map, LatLng position) {
        Log.d(TAG, "setMapLocation: ");
        // Add a marker for this item and set the camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
        map.addMarker(new MarkerOptions().position(position));

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
    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback{

        MapView mapView;
        TextView text;
        GoogleMap map;

        public EventViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.info_text);
            mapView = (MapView) itemView.findViewById(R.id.map_view);
            initializeMapView();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: ");
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(PollutionApplication.getAppContext());
            map = googleMap;
            LatLng data = (LatLng) mapView.getTag();
            if (data != null) {
                setMapLocation(map, data);
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
    }


}
