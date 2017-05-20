package com.supsi.alessandro.pollutionproblemclient.ble;

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

    private static final PollutionBleCentral mInstance = new PollutionBleCentral();
    private static final String TAG = PollutionBleCentral.class.getSimpleName();
    private BleManager mBleManager;

    private PollutionBleCentral() {
        mBleManager = BleManager.getInstance();
    }

    public static PollutionBleCentral getInstance() {
        return mInstance;
    }

    public void discoverPollutionDevices() {
        if (mBleManager.isBleEnabled()) {

            Log.i(TAG, "discoverPollutionDevices() ---> Ble is enabled");

            List<ScanFilter> scanFilters = buildScanFilters();

            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();

            mBleManager.startBleScan(scanFilters, settings, new BleScanCallBack());
        } else {
            Log.i(TAG, "discoverPollutionDevices() ---> Ble is NOT enabled");
        }

    }

    @NonNull
    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();
        scanFilters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(BleConstants.SERVICE_TO_DISCOVER_UUID))).build());
        return scanFilters;
    }

    /**
     *
     */
    private class BleScanCallBack extends ScanCallback {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i(TAG, "onScanResult() ---> " + result.getDevice().getName());
            mBleManager.stopBleScan(this);
            super.onScanResult(callbackType, result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.i(TAG, "onBatchScanResults()");
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.i(TAG, "onScanFailed()");
            super.onScanFailed(errorCode);
        }
    }
}
