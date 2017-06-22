package com.supsi.alessandro.pollutionproblemclient.activities;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.supsi.alessandro.pollutionproblemclient.Constants;
import com.supsi.alessandro.pollutionproblemclient.R;
import com.supsi.alessandro.pollutionproblemclient.ble.BleConstants;
import com.supsi.alessandro.pollutionproblemclient.ble.PollutionDeviceConnectService;
import com.supsi.alessandro.pollutionproblemclient.ble.PollutionDevicesScanActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private PollutionDeviceConnectService mPollDeviceConnectService;
    private BluetoothDevice mPollDeviceToConnect;
    private TextView mPollutionDataTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.b_start_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick() ---> clicked start scan button");
                Intent i = new Intent(MainActivity.this, PollutionDevicesScanActivity.class);
                startActivityForResult(i, BleConstants.POLL_DEVICES_SCAN_ACTIVITY_RESULT);
            }
        });

        findViewById(R.id.b_start_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick() ---> clicked start map button");
                Intent i = new Intent(MainActivity.this, MapActivity.class);
                startActivity(i);
            }
        });

        mPollutionDataTextview = (TextView) findViewById(R.id.tv_poll_data);
        mPollutionDataTextview.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start the pollution devices connect service
        Intent intent = new Intent(this, PollutionDeviceConnectService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mServiceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mPollutionDataReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mPollutionDataReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BleConstants.POLL_DEVICES_SCAN_ACTIVITY_RESULT: {
                if (data != null && data.hasExtra(Constants.EXTRA_ACTIVITY_RESULT)) {
                    BluetoothDevice selectedDevice = data.getParcelableExtra(Constants.EXTRA_ACTIVITY_RESULT);
                    Log.d(TAG, "onActivityResult() --> selected device : " + selectedDevice.getName());
                    mPollDeviceToConnect = selectedDevice;

                }
            }
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(TAG, "onServiceConnected()");
            if (mPollDeviceToConnect != null) {
                mPollDeviceConnectService = ((PollutionDeviceConnectService.LocalBinder) service).getService();
                mPollDeviceConnectService.connectPollutionDevice(mPollDeviceToConnect);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected()");
            mPollDeviceConnectService = null;
        }
    };

    private final BroadcastReceiver mPollutionDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Constants.ACTION_POLLUTION_UPDATE.equals(action)) {
                mPollutionDataTextview.append(intent.getStringExtra(Constants.EXTRA_NEW_POLLUTION_DATA)+"\n");
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_POLLUTION_UPDATE);
        return intentFilter;
    }
}
