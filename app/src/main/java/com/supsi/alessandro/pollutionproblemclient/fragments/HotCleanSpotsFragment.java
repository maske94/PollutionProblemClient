package com.supsi.alessandro.pollutionproblemclient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.supsi.alessandro.pollutionproblemclient.R;
import com.supsi.alessandro.pollutionproblemclient.adapters.HotCleanSpotsAdapter;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.storage.content_provider.PollutionProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by Alessandro on 11/07/2017.
 *
 * Shows to the user most polluted places and most clean places.
 * Harnesses recycler view with cards.
 *
 */
public class HotCleanSpotsFragment extends Fragment {

    private RecyclerView mHotSpotsRecyclerView;
    private RecyclerView mCleanSpotsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hot_clean_spots, container, false);
        mHotSpotsRecyclerView = (RecyclerView) v.findViewById(R.id.hot_spots_recycler_view);
        mCleanSpotsRecyclerView = (RecyclerView) v.findViewById(R.id.clean_spots_recycler_view);

        ArrayList<Event> dataSet = PollutionProvider.getEvents("maske94", "prova", null, null, getContext().getContentResolver());

        Collections.sort(dataSet, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return (int)(e2.getDoublePollutionValue()-e1.getDoublePollutionValue());
            }
        });


        mHotSpotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCleanSpotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mHotSpotsRecyclerView.setAdapter(new HotCleanSpotsAdapter(dataSet.subList(0,3) ));
        mCleanSpotsRecyclerView.setAdapter(new HotCleanSpotsAdapter( dataSet.subList(dataSet.size()-4,dataSet.size()-1)));
        return v;

    }




    }
