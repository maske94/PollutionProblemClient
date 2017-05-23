package com.supsi.alessandro.pollutionproblemclient.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.supsi.alessandro.pollutionproblemclient.PollutionApplication;

import java.util.List;

/**
 * Created by Alessandro on 20/05/2017.
 * <p>
 * BLE settings manipulation.
 * BLE devices discovery.
 * BLE devices connection.
 */

class BleManager {

    private static final String TAG = BleManager.class.getSimpleName();
    private static final BleManager mInstance = new BleManager();
    private BluetoothAdapter mBluetoothAdapter;

    private BleManager() {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) PollutionApplication.getAppContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    static BleManager getInstance() {
        return mInstance;
    }

    /**
     * Check whether the ble is enabled or not.
     *
     * @return true if bluetooth is enabled, false otherwise
     */
    boolean isBleEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * If bluetooth is not enabled starts a new activity that requests to enable the bluetooth.
     * Otherwise does nothing.
     *
     * @param activity activity from where start the new activity
     */
    public void enableBle(Activity activity) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, BleConstants.REQUEST_ENABLE_BT);
        }
    }

    /**
     * Starts a ble scanning with the new BluetoothLeScanner object available from API 21.
     * After a defined period stops automatically the scan, in order to save battery life.
     *
     * @param scanFilters
     * @param scanSettings
     * @param scanCallback
     */
    void startBleScan(List<ScanFilter> scanFilters, ScanSettings scanSettings, final ScanCallback scanCallback) {

        final BluetoothLeScanner mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback);

        // In order to save battery we stop the scanning after a defined period
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "stopScan()");
                mBluetoothLeScanner.stopScan(scanCallback);
            }
        }, BleConstants.SCAN_PERIOD);
    }

    /**
     * Stop the ble devices scanning.
     *
     * @param scanCallback
     */
    void stopBleScan(ScanCallback scanCallback) {
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }

    /**
     * Connects to a ble peripheral device.
     *
     * @param device
     * @param mBluetoothGattCallback
     */
    void connectToDevice(BluetoothDevice device, BluetoothGattCallback mBluetoothGattCallback) {
        device.connectGatt(PollutionApplication.getAppContext(), false, mBluetoothGattCallback);
    }


}
