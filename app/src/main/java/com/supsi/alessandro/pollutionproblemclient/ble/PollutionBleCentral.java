package com.supsi.alessandro.pollutionproblemclient.ble;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alessandro on 21/05/2017.
 * <p>
 * Discovers and connects with pollution wearable devices only, by exploiting BleManager.java
 * Reads services and characteristics.
 */

public class PollutionBleCentral {

    private static final String TAG = PollutionBleCentral.class.getSimpleName();

    private static final String SERVICE_TO_DISCOVER = BleConstants.SERVICE_HEART_MONITOR_UUID;
    private static final String CHARACTERISTIC_TO_DISCOVER = BleConstants.HEART_RATE_MEASUREMENT_CHARACTERISTIC_UUID;

    private static final PollutionBleCentral mInstance = new PollutionBleCentral();
    private BleManager mBleManager;

    private PollutionBleCentral() {
        mBleManager = BleManager.getInstance();
    }

    public static PollutionBleCentral getInstance() {
        return mInstance;
    }

    /**
     * Starts a ble discovery
     */
    public void discoverPollutionDevices(Activity activity) {
        if (mBleManager.isBleEnabled()) {
            Log.i(TAG, "discoverPollutionDevices() ---> Ble is enabled");

            if (!mBleManager.askForCoarseLocationPermission(activity))
                return;

            List<ScanFilter> scanFilters = buildScanFilters();
            ScanSettings settings = buildScanSettings();

            mBleManager.startBleScan(scanFilters, settings, new BleScanCallBack());
        } else {
            Log.i(TAG, "discoverPollutionDevices() ---> Ble is NOT enabled");
            mBleManager.enableBle(activity);
        }
    }

    /**
     * @return
     */
    private ScanSettings buildScanSettings() {
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
    }

    /**
     * @return
     */
    @NonNull
    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();
        scanFilters.add(new ScanFilter.Builder()
                .setServiceUuid(new ParcelUuid(UUID.fromString(SERVICE_TO_DISCOVER)))
                .build());
        return scanFilters;
    }

    /**
     * Callback called when a ble device is discovered
     */
    private class BleScanCallBack extends ScanCallback {

        private boolean enabled = true;

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (enabled) { // boolean used to immediately stop the scan result callback.
                Log.i(TAG, "onScanResult() ---> " + result.getDevice().getName());
                mBleManager.stopBleScan(this);
                mBleManager.connectToDevice(result.getDevice(), new BleConnectionCallback());
                enabled = false;
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.i(TAG, "onBatchScanResults()");
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.i(TAG, "onScanFailed()");
        }
    }

    /**
     * Callback called when connected to a ble device
     */
    private class BleConnectionCallback extends BluetoothGattCallback {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (BluetoothGatt.GATT_SUCCESS == status) {
                if (BluetoothGatt.STATE_CONNECTED == newState) {
                    Log.i(TAG, "onConnectionStateChange() ---> Connected to " + gatt.getDevice().getName());
                    gatt.discoverServices();
                } else if (BluetoothGatt.STATE_DISCONNECTED == newState) {
                    Log.i(TAG, "onConnectionStateChange() ---> Disconnected from " + gatt.getDevice().getName());
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            List<BluetoothGattService> services = gatt.getServices();
            Log.i(TAG, "onServicesDiscovered() ---> " + gatt.getDevice().getName() + " has " + services.size() + " services.");

            /**
             * For each service discovered
             */
            for (BluetoothGattService s : services) {

                Log.i(TAG, "\t---> service UUID: " + s.getUuid());

                // Check if the current service is the one we are looking for
                if (UUID.fromString(SERVICE_TO_DISCOVER).equals(s.getUuid())) {

                    List<BluetoothGattCharacteristic> characteristics = s.getCharacteristics();
                    Log.i(TAG, "\t\t---> characteristics number :" + characteristics.size());

                    for (BluetoothGattCharacteristic c : characteristics) {

                        Log.i(TAG, "\t\t\t---> characteristic UUID :" + c.getUuid());

                        // Check for the wanted characteristic
                        if (UUID.fromString(CHARACTERISTIC_TO_DISCOVER).equals(c.getUuid())) {

                            Log.i(TAG, "\t\t\t\t---> Found the wanted characteristic " + c.getUuid() + " , enabling notification...");

                            /**
                             * Enabling notification for the given characteristic
                             */
                            //gatt.readCharacteristic(c);
                            mBleManager.enableNotification(gatt, c);
                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(TAG, "onCharacteristicRead() ---> " + characteristic.getUuid());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.i(TAG, "onCharacteristicChanged() ---> " + characteristic.getUuid());

            if (UUID.fromString(CHARACTERISTIC_TO_DISCOVER).equals(characteristic.getUuid())) {

//                byte[] bytes = characteristic.getValue();
//                String str = new String(bytes);
//                Log.i(TAG, "\t--> received string: " + str);
//
//                if (data != null && data.length > 0) {
//                    final StringBuilder stringBuilder = new StringBuilder(data.length);
//                    for (byte byteChar : data) {
//
//                        stringBuilder.append(String.format("%02X ", byteChar));
//                        Log.v(TAG, String.format("%02X ", byteChar));
//                    }
//                    Log.i(TAG, "onCharacteristicChanged() --> read string: " + stringBuilder);
//                }

                // Heart measurement monitor characteristic
                int flag = characteristic.getProperties();
                int format = -1;
                if ((flag & 0x01) != 0) {
                    format = BluetoothGattCharacteristic.FORMAT_UINT16;
                    //Log.d(TAG, "                           ---> Heart rate format UINT16.");
                } else {
                    format = BluetoothGattCharacteristic.FORMAT_UINT8;
                    //Log.d(TAG, "                           ---> Heart rate format UINT8.");
                }
                final int heartRate = characteristic.getIntValue(format, 1);
                Log.d(TAG, "\t---> Received heart rate: " + heartRate);
            }
        }
    }

}
