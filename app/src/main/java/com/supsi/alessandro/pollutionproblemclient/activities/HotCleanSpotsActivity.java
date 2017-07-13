package com.supsi.alessandro.pollutionproblemclient.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.supsi.alessandro.pollutionproblemclient.R;
import com.supsi.alessandro.pollutionproblemclient.adapters.HotCleanSpotsAdapter;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.storage.content_provider.PollutionProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Alessandro on 11/07/2017.
 * <p>
 * Shows to the user most polluted places and most clean places.
 * Harnesses recycler view with cards.
 */
public class HotCleanSpotsActivity extends AppCompatActivity {

    private RecyclerView mHotSpotsRecyclerView;
    private RecyclerView mCleanSpotsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_clean_spots);


        mHotSpotsRecyclerView = (RecyclerView) findViewById(R.id.hot_spots_recycler_view);
        mCleanSpotsRecyclerView = (RecyclerView) findViewById(R.id.clean_spots_recycler_view);

        ArrayList<Event> dataSet = PollutionProvider.getEvents("maske94", "prova", null, null, getContentResolver());

        Collections.sort(dataSet, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return (int) (e2.getDoublePollutionValue() - e1.getDoublePollutionValue());
            }
        });


        mHotSpotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCleanSpotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mHotSpotsRecyclerView.setAdapter(new HotCleanSpotsAdapter(this,dataSet.subList(0, 3)));
        mCleanSpotsRecyclerView.setAdapter(new HotCleanSpotsAdapter(this,dataSet.subList(dataSet.size() - 4, dataSet.size() - 1)));
    }

}
