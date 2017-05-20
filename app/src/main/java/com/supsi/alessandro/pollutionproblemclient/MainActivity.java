package com.supsi.alessandro.pollutionproblemclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.supsi.alessandro.pollutionproblemclient.ble.PollutionBleCentral;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PollutionBleCentral.getInstance().discoverPollutionDevices();
    }
}
