package com.supsi.alessandro.pollutionproblemclient.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import com.supsi.alessandro.pollutionproblemclient.Constants;
import com.supsi.alessandro.pollutionproblemclient.PollutionApplication;

/**
 * Created by Alessandro on 20/05/2017.
 * <p>
 * BLE settings manipulation.
 * BLE devices discovery.
 * BLE devices connection.
 */

class BleManager {

    private static final BleManager mBleManagerInstance = new BleManager();
    private BluetoothAdapter mBluetoothAdapter;

    private BleManager() {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) PollutionApplication.getAppContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    public static BleManager getInstance() {
        return mBleManagerInstance;
    }

    /**
     * Check whether the ble is enabled or not.
     * @return true if bluetooth is enabled, false otherwise
     */
    public boolean isBleEnabled(){
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * If bluetooth is not enabled starts a new activity that requests to enable the bluetooth.
     * Otherwise does nothing.
     */
    public void enableBle(Activity activity) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
        }
    }
}
