package com.supsi.alessandro.pollutionproblemclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.supsi.alessandro.pollutionproblemclient.ble.PollutionDevicesScanActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.b_start_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick() ---> clicked start scan button");
                Intent i = new Intent(MainActivity.this,PollutionDevicesScanActivity.class);
                startActivity(i);
            }
        });

    }


}
