package com.supsi.alessandro.pollutionproblemclient.ble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.supsi.alessandro.pollutionproblemclient.PollutionApplication;

import java.util.List;
import java.util.UUID;

/**
 * Created by Alessandro on 20/05/2017.
 * <p>
 * Singleton class for:
 *
 * BLE settings manipulation.
 * BLE devices discovery.
 * BLE devices connection/disconnection.
 */

class BleManager {

    private static final String TAG = BleManager.class.getSimpleName();
    private static final BleManager mInstance = new BleManager();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    /**
     *
     */
    private BleManager() {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) PollutionApplication.getAppContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    /**
     *
     * @return A singleton instance of this class
     */
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
    void enableBle(Activity activity) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, BleConstants.REQUEST_ENABLE_BT);
        }
    }

    /**
     * Starts a ble scanning with the new BluetoothLeScanner object available from API 21.
     * After a defined period stops automatically the scan, in order to save battery life.
     *
     * @param scanFilters Filters to apply during the scan
     * @param scanSettings Scan settings
     * @param scanCallback Callback to call after the scan
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
     * @param scanCallback Callback to call after stop the scan
     */
    void stopBleScan(ScanCallback scanCallback) {
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }

    /**
     * Connects to a ble peripheral device.
     *
     * @param device The peripheral device
     * @param mBluetoothGattCallback The callback to call after connecting
     */
    void connectToDevice(BluetoothDevice device, BluetoothGattCallback mBluetoothGattCallback) {
        mBluetoothGatt = device.connectGatt(PollutionApplication.getAppContext(), false, mBluetoothGattCallback);
    }

    /**
     * Connects to a ble peripheral device.
     *
     * @param deviceAddress The mac address of the ble peripheral device
     * @param mBluetoothGattCallback The callback to call after connecting
     * @return True if the device if found and it's possible to start a connection
     *         False if the given address doesn't exist or in not reachable
     */
    boolean connectToDevice(String deviceAddress, BluetoothGattCallback mBluetoothGattCallback) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
        if (device == null) {
            Log.w(TAG, "connectToDevice() ---> Device address not found.  Unable to connect.");
            return false;
        }
        mBluetoothGatt = device.connectGatt(PollutionApplication.getAppContext(), false, mBluetoothGattCallback);
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Enables notifications for the given characteristic
     *
     * @param gatt the BluetoothGatt towards perform the request
     * @param c the characteristic to enable the notification
     */
    void enableNotification(BluetoothGatt gatt, BluetoothGattCharacteristic c) {
        gatt.setCharacteristicNotification(c, true);
        // Get the configuration descriptor and enable the notification
        // 0x2902 org.bluetooth.descriptor.gatt.client_characteristic_configuration.xml
        UUID uuid = UUID.fromString(BleConstants.CHARACTERISTIC_CONFIGURATION_UUID);
        BluetoothGattDescriptor descriptor = c.getDescriptor(uuid);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    /**
     * If the target sdk version is >= api 23 ( Android 6 (M) )
     * Asks to the user for permission to access the coarse location.
     * This is needed from Android 6 to read ble scanning results.
     *
     * @param activity The activity from where the request is launched
     * @return True if the permission was already granted, false otherwise
     */
    boolean askForCoarseLocationPermission(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    BleConstants.PERMISSIONS_REQUEST_COARSE_LOCATION);
            return false;
        }
        return true;
    }


}
