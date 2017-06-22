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
import com.supsi.alessandro.pollutionproblemclient.PollutionApplication;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Alessandro on 21/05/2017.
 * <p>
 * Service that connects to a pollution wearable device only, by exploiting BleManager.java
 * Reads defined services and characteristics of a pollution device.
 */
public class PollutionDeviceConnectService extends Service {

    private static final String TAG = PollutionDeviceConnectService.class.getSimpleName();
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

    /**
     * Connect to the given {@link BluetoothDevice} device
     *
     * @param device The device which connect to
     */
    public void connectPollutionDevice(BluetoothDevice device) {
        mBleManager.connectToDevice(device, new BleConnectionCallback());
    }

    /**
     * Callback called when connected to a ble device
     */
    private class BleConnectionCallback extends BluetoothGattCallback {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (BluetoothGatt.GATT_SUCCESS == status) {
                Log.i(TAG, "onConnectionStateChange() ---> new state: " + newState);
                if (BluetoothGatt.STATE_CONNECTED == newState) {
                    Log.i(TAG, "onConnectionStateChange() ---> Connected to " + gatt.getDevice().getName());
                    gatt.discoverServices();
                } else if (BluetoothGatt.STATE_DISCONNECTED == newState) {
                    Log.i(TAG, "onConnectionStateChange() ---> Disconnected from " + gatt.getDevice().getName());
                    mBleManager.close();
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
                if (UUID.fromString(BleConstants.SERVICE_TO_DISCOVER).equals(s.getUuid())) {

                    List<BluetoothGattCharacteristic> characteristics = s.getCharacteristics();
                    Log.i(TAG, "\t\t---> characteristics number :" + characteristics.size());

                    for (BluetoothGattCharacteristic c : characteristics) {

                        Log.i(TAG, "\t\t\t---> characteristic UUID :" + c.getUuid());

                        // Check for the wanted characteristic
                        if (UUID.fromString(BleConstants.CHARACTERISTIC_TO_DISCOVER).equals(c.getUuid())) {

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

            if (UUID.fromString(BleConstants.CHARACTERISTIC_TO_DISCOVER).equals(characteristic.getUuid())) {

//                byte[] bytes = characteristic.getValue();
//
//                //Log.d(TAG, "\t---> received bytes: " + Arrays.toString(bytes));
//                //String str = new String(bytes);
//                //Log.i(TAG, "\t--> received string: " + str);
//
//                if (bytes != null && bytes.length > 0) {
//                    final StringBuilder stringBuilder = new StringBuilder(bytes.length);
//                    for (byte byteChar : bytes) {
//                        stringBuilder.append(String.format("%02X ", byteChar));
//                        //Log.v(TAG, String.format("%02X ", byteChar));
//                    }
//                    Log.i(TAG, "onCharacteristicChanged() --> read string: " + stringBuilder);
//                    Event event = buildEventFromBytesArray(PollutionApplication.getLoggedUsername(), PollutionApplication.getChildId(gatt.getDevice().getAddress()), bytes);
//
//                    if (event == null) {
//                        Log.e(TAG, "onCharacteristicChanged() ---> event is NULL");
//                    } else {
//                        Log.d(TAG, "onCharacteristicChanged() ---> generated event: " + event);
//                        sendBroadcast(event.toString());
//                    }
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
                sendBroadcast(heartRate + "");
            }
        }
    }

    /**
     * Creates a intent and send it to the broadcast receivers.
     *
     * @param s The string to be send to the broadcast receivers
     */
    private void sendBroadcast(String s) {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_POLLUTION_UPDATE);
        intent.putExtra(Constants.EXTRA_NEW_POLLUTION_DATA, s);
        sendBroadcast(intent);
    }

    /**
     * Converts an array of bytes into an {@link Event} object.
     * The conversion is done by following the protocol signed with the wearable device that sends the array of bytes.
     * The wearable device sends 19 bytes in total, their meaning is as follows:
     * <p>
     * Bytes     0 : year of the timestamp as integer
     * Byte      1 : month of the timestamp as integer
     * Byte      2 : day of the timestamp as integer
     * Byte      3 : hour of the timestamp as integer
     * Byte      4 : minutes of the timestamp as integer
     * Byte      5 : seconds of the timestamp as integer
     * Bytes   6-9 : pollution value as float
     * Bytes 10-13 : gps latitude as float
     * Bytes 14-17 : gps longitude as float
     *
     * @param bytes The array of bytes to be converted
     * @return The converted {@link Event} object
     */
    public static Event buildEventFromBytesArray(String username, String childId, byte[] bytes) {

        // Check on the bytes array dimension
        if (bytes.length != 18) {
            Log.e(TAG, "buildEventFromBytesArray: The given array of bytes MUST contain 19 bytes");
            return null;
        }

        /**
         * Create timestamp.
         */
        int year = bytes[0];
        // Add the constant 2000 to the year because we send only the
        // 2 number of the year from the wearable device to the smart phone
        // in order to save bytes
        year += 2000;
        int month = bytes[1];
        int day = bytes[2];
        int hour = bytes[3];
        int minute = bytes[4];
        int second = bytes[5];

        // The next line formats the previous integers into an ISO date standard format.
        // It also ensures that, if some integer is 1 digit only, additional 0 are added before
        // In order to have a correct ISO format.
        String isoTimestamp = String.format(Locale.ITALY, "%04d-%02d-%02dT%02d:%02d:%02d", year, month, day, hour, minute, second);

        /**
         * Create pollution value
         */
        byte[] pollValueBytes = {bytes[6], bytes[7], bytes[8], bytes[9]};
        float pollValue = ByteBuffer.wrap(pollValueBytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();

        /**
         * Create gps lat
         */
        byte[] gpsLatBytes = {bytes[10], bytes[11], bytes[12], bytes[13]};
        float gpsLat = ByteBuffer.wrap(gpsLatBytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();

        /**
         * Create gps long
         */
        byte[] gpsLongBytes = {bytes[14], bytes[15], bytes[16], bytes[17]};
        float gpsLong = ByteBuffer.wrap(gpsLongBytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();

        /**
         * Generate and return the new event
         */
        if (username == null) {
            Log.e(TAG, "buildEventFromBytesArray() --> stored username is NULL");
        }

        return new Event(username, childId, pollValue, isoTimestamp, gpsLat, gpsLong);
    }

}
