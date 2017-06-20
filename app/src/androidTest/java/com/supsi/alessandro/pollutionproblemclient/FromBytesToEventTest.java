package com.supsi.alessandro.pollutionproblemclient;

import android.support.test.runner.AndroidJUnit4;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.ble.PollutionDeviceConnectService;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by Alessandro on 20/06/2017.
 */
@RunWith(AndroidJUnit4.class)
public class FromBytesToEventTest {

    @Test
    public void convertedEventIsCorrect() throws Exception {
        byte[] bytes = {0x11, 0x06, 0x14, 0x0B, 0x37, 0x23, 0x52, (byte) 0xB8, 0x1E, 0x3F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        Event targetEvent = new Event("maske94", "pluto", "0.62", "2017-06-20T11:55:35", "0.0", "0.0");
        Event convertedEvent = PollutionDeviceConnectService.buildEventFromBytesArray(null, null, bytes);

        assertNotNull(convertedEvent);
        assertEquals(targetEvent.getPollutionValue(), convertedEvent.getPollutionValue());
        assertEquals(targetEvent.getTimeStamp(), convertedEvent.getTimeStamp());
        assertEquals(targetEvent.getGpsLat(), convertedEvent.getGpsLat());
        assertEquals(targetEvent.getGpsLong(), convertedEvent.getGpsLong());
    }

    @Test
    public void timestampBuildingIsCorrect() throws Exception {
        byte[] bytes = {0x01, 0x01, 0x09, 0x0A, 0x0B, 0x0C, // Timestamp
                0x52, (byte) 0xB8, 0x1E, 0x3F, // Pollution value
                0x00, 0x00, 0x00, 0x00, // gps lat
                0x00, 0x00, 0x00, 0x00};// gps long

        Event targetEvent = new Event("maske94", "pluto", "0.62", "2001-01-09T10:11:12", "0.0", "0.0");
        Event convertedEvent = PollutionDeviceConnectService.buildEventFromBytesArray(null, null, bytes);

        assertNotNull(convertedEvent);
        assertEquals(targetEvent.getPollutionValue(), convertedEvent.getPollutionValue());
        assertEquals(targetEvent.getTimeStamp(), convertedEvent.getTimeStamp());
        assertEquals(targetEvent.getGpsLat(), convertedEvent.getGpsLat());
        assertEquals(targetEvent.getGpsLong(), convertedEvent.getGpsLong());
    }

    @Test
    public void arraySizeIsNotCorrect() {
        byte[] bytes = {0x01, 0x01}; // Array size must be 18 bytes, now is 2 bytes.
        Event convertedEvent = PollutionDeviceConnectService.buildEventFromBytesArray(null,null,bytes);
        assertNull(convertedEvent);
    }
}
