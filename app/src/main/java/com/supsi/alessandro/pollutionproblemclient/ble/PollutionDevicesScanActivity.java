package com.supsi.alessandro.pollutionproblemclient.ble;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.supsi.alessandro.pollutionproblemclient.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alessandro on 25/05/2017.
 * <p>
 * An activity that scans BLE devices with given filters and shows the result on a list view.
 */
public class PollutionDevicesScanActivity extends ListActivity {

    private static final String TAG = PollutionDevicesScanActivity.class.getSimpleName();
    private static final String SERVICE_TO_DISCOVER = BleConstants.SERVICE_HEART_MONITOR_UUID;

    private BleManager mBleManager;
    private PollutionDeviceListAdapter mPollutionDeviceListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBleManager = BleManager.getInstance();

        // Initializes list view adapter.
        mPollutionDeviceListAdapter = new PollutionDeviceListAdapter();
        setListAdapter(mPollutionDeviceListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBleManager.initialize();
        discoverPollutionDevices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBleManager.stopBleScan(new PollutionScanCallback());
        mBleManager.close();
    }

    /**
     * Starts pollution devices discovery.
     * Check if ble is enabled in the device.
     * Check if there is the COARSE_LOCATION_PERMISSION in device with Android API >= 23
     */
    private void discoverPollutionDevices() {
        if (mBleManager.isBleEnabled()) {
            Log.i(TAG, "discoverPollutionDevices() ---> Ble is enabled");

            if (!mBleManager.askForCoarseLocationPermission(this))
                return;

            List<ScanFilter> scanFilters = buildScanFilters();
            ScanSettings settings = buildScanSettings();

            mBleManager.startBleScan(scanFilters, settings, new PollutionScanCallback());
        } else {
            Log.i(TAG, "discoverPollutionDevices() ---> Ble is NOT enabled, asking to enable it...");
            mBleManager.askBleEnabling(this);
        }
    }

    /**
     * @return Scan settings for pollution devices scanning
     */
    private ScanSettings buildScanSettings() {
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
    }

    /**
     * @return List of filters to discover only pollution devices
     */
    @NonNull
    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();
        scanFilters.add(new ScanFilter.Builder()
                .setServiceUuid(new ParcelUuid(UUID.fromString(SERVICE_TO_DISCOVER)))
                .build());
        return scanFilters;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case BleConstants.PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult() ---> PERMISSIONS_REQUEST_COARSE_LOCATION granted");
                    discoverPollutionDevices();
                } else {
                    Log.i(TAG, "onRequestPermissionsResult() ---> PERMISSIONS_REQUEST_COARSE_LOCATION denied");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BleConstants.REQUEST_ENABLE_BT: {
                discoverPollutionDevices();
            }
        }
    }

    /**
     * Callback called when a pollution ble device is discovered
     */
    private class PollutionScanCallback extends ScanCallback {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i(TAG, "onScanResult() ---> " + result.getDevice().getName());
            mPollutionDeviceListAdapter.addDevice(result.getDevice());
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

    private class PollutionDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflater;

        PollutionDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<>();
            mInflater = PollutionDevicesScanActivity.this.getLayoutInflater();
        }

        void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                notifyDataSetChanged();
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            // General ListView optimization code.
            if (view == null) {
                view = mInflater.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();

            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);

            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }

        /**
         * View of a single item of the list
         */
        class ViewHolder {
            TextView deviceName;
            TextView deviceAddress;
        }
    }

}
