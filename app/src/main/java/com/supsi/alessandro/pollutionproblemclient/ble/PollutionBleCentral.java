package com.supsi.alessandro.pollutionproblemclient.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
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
    private static final PollutionBleCentral mInstance = new PollutionBleCentral();
    private BleManager mBleManager;

    private PollutionBleCentral() {
        mBleManager = BleManager.getInstance();
    }

    public static PollutionBleCentral getInstance() {
        return mInstance;
    }

    /**
     *
     */
    public void discoverPollutionDevices() {
        if (mBleManager.isBleEnabled()) {

            Log.i(TAG, "discoverPollutionDevices() ---> Ble is enabled");

            List<ScanFilter> scanFilters = buildScanFilters();
            ScanSettings settings = buildScanSettings();

            mBleManager.startBleScan(scanFilters, settings, new BleScanCallBack());
        } else {
            Log.i(TAG, "discoverPollutionDevices() ---> Ble is NOT enabled");
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
                .setServiceUuid(new ParcelUuid(UUID.fromString(BleConstants.SERVICE_TO_DISCOVER_UUID)))
                .build());
        return scanFilters;
    }

    /**
     *
     */
    private class BleScanCallBack extends ScanCallback {

        private boolean enabled = true;

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(enabled) { // Used to immediately stop the scan result callback.
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
     *
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
            for (BluetoothGattService s : services) {
                Log.i(TAG, "                   ---> new service UUID: " + s.getUuid());
                // Force the characteristics read for each service of the defined type
                if (UUID.fromString(BleConstants.SERVICE_TO_DISCOVER_UUID).equals(s.getUuid())) {
                    List<BluetoothGattCharacteristic> characteristics = s.getCharacteristics();
                    Log.i(TAG, "                              ---> characteristics number :" + characteristics.size());
                    for (BluetoothGattCharacteristic c : characteristics) {
                        Log.i(TAG, "                                     ---> characteristic UUID :" + c.getUuid());
                        // Check for the wanted characteristic
                        if (UUID.fromString(BleConstants.HEART_RATE_MEASUREMENT_UUID).equals(c.getUuid())) {
                            Log.i(TAG, "                                     ---> Found the wanted characteristic " + c.getUuid() + " , enabling notification...");
                            //gatt.readCharacteristic(c);
                            gatt.setCharacteristicNotification(c, true);
                            // 0x2902 org.bluetooth.descriptor.gatt.client_characteristic_configuration.xml
                            UUID uuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
                            BluetoothGattDescriptor descriptor = c.getDescriptor(uuid);
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(descriptor);
                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(TAG, "onCharacteristicRead() ---> " + characteristic.getUuid());

            if (UUID.fromString(BleConstants.HEART_RATE_MEASUREMENT_UUID).equals(characteristic.getUuid())) {
                int flag = characteristic.getProperties();
                int format = -1;
                if ((flag & 0x01) != 0) {
                    format = BluetoothGattCharacteristic.FORMAT_UINT16;
                    Log.d(TAG, "onCharacteristicRead() ---> Heart rate format UINT16.");
                } else {
                    format = BluetoothGattCharacteristic.FORMAT_UINT8;
                    Log.d(TAG, "onCharacteristicRead() ---> Heart rate format UINT8.");
                }
                final int heartRate = characteristic.getIntValue(format, 1);
                Log.d(TAG, "onCharacteristicRead() ---> Received heart rate: " + heartRate);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.i(TAG, "onCharacteristicChanged()");
            Log.i(TAG, "                           ---> " + characteristic.getUuid());

            if (UUID.fromString(BleConstants.HEART_RATE_MEASUREMENT_UUID).equals(characteristic.getUuid())) {
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
                Log.d(TAG, "                           ---> Received heart rate: " + heartRate);
            }
        }
    }
}
