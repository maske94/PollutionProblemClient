package com.supsi.alessandro.pollutionproblemclient.ble;

/**
 * Created by Alessandro on 20/05/2017.
 */

public class BleConstants {

    // Activity request codes
    static final int REQUEST_ENABLE_BT = 1;
    public static final int POLL_DEVICES_SCAN_ACTIVITY_RESULT = 2;
    static final int PERMISSIONS_REQUEST_COARSE_LOCATION = 3;

    static final long SCAN_PERIOD = 10000L; // in milliseconds

    // Services UUID
    static final String SERVICE_HEART_MONITOR_UUID = "0000180D-0000-1000-8000-00805F9B34FB";
    static final String SERVICE_H10_RADIO_UUID = "0000FFE0-0000-1000-8000-00805F9B34FB";

    // Characteristic UUID
    static final String HEART_RATE_MEASUREMENT_CHARACTERISTIC_UUID = "00002A37-0000-1000-8000-00805F9B34FB";
    static final String H10_RADIO_CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    static final String CHARACTERISTIC_CONFIGURATION_UUID = "00002902-0000-1000-8000-00805f9b34fb";

}
