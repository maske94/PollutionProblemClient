package com.supsi.alessandro.pollutionproblemclient.ble;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.supsi.alessandro.pollutionproblemclient.Constants;

import java.util.List;
import java.util.UUID;

/**
 * Created by Alessandro on 21/05/2017.
 * <p>
 * Service that connects to a pollution wearable device only, by exploiting BleManager.java
 * Reads defined services and characteristics of a pollution device.
 */
public class PollutionDeviceConnectService extends Service {

    private static final String TAG = PollutionDeviceConnectService.class.getSimpleName();
    private static final String SERVICE_TO_DISCOVER = BleConstants.SERVICE_HEART_MONITOR_UUID;
    private static final String CHARACTERISTIC_TO_DISCOVER = BleConstants.HEART_RATE_MEASUREMENT_CHARACTERISTIC_UUID;

    private BleManager mBleManager;

    /**
     * Service binding related stuffs
     */

    public class LocalBinder extends Binder {
        public PollutionDeviceConnectService getService() {
            return PollutionDeviceConnectService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        mBleManager = BleManager.getInstance();
        mBleManager.initialize();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        mBleManager.close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    public void connectPollutionDevice(BluetoothDevice device){
        mBleManager.connectToDevice(device,new BleConnectionCallback());
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

                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_POLLUTION_UPDATE);
                intent.putExtra(Constants.EXTRA_NEW_POLLUTION_DATA,heartRate+"");
                sendBroadcast(intent);
            }
        }
    }

}
