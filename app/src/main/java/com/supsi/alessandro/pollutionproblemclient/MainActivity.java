package com.supsi.alessandro.pollutionproblemclient;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.supsi.alessandro.pollutionproblemclient.ble.BleConstants;
import com.supsi.alessandro.pollutionproblemclient.ble.PollutionBleCentral;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private PollutionBleCentral mPollutionBleCentral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPollutionBleCentral = PollutionBleCentral.getInstance();
        mPollutionBleCentral.discoverPollutionDevices(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case BleConstants.PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult() ---> PERMISSIONS_REQUEST_COARSE_LOCATION granted");
                    mPollutionBleCentral.discoverPollutionDevices(this);
                } else {
                    Log.i(TAG, "onRequestPermissionsResult() ---> PERMISSIONS_REQUEST_COARSE_LOCATION denied");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case BleConstants.REQUEST_ENABLE_BT:{
                mPollutionBleCentral.discoverPollutionDevices(this);
            }
        }
    }
}
