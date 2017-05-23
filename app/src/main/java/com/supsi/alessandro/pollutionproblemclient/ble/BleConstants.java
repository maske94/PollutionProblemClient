package com.supsi.alessandro.pollutionproblemclient.ble;

/**
 * Created by Alessandro on 20/05/2017.
 */

public class BleConstants {
    public static final int REQUEST_ENABLE_BT = 1;
    static final long SCAN_PERIOD = 10000L; // in milliseconds
    static final String SERVICE_TO_DISCOVER_UUID = "0000180D-0000-1000-8000-00805F9B34FB";
    static final String HEART_RATE_MEASUREMENT_UUID = "00002A37-0000-1000-8000-00805F9B34FB";
    public static final int PERMISSIONS_REQUEST_COARSE_LOCATION = 2;
}
