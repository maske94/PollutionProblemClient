package com.supsi.alessandro.pollutionproblemclient;

import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.storage.content_provider.PollutionProvider;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Alessandro on 19/06/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {

    private static ContentResolver contentResolver;

    @BeforeClass
    public static void setUp() {
        contentResolver = PollutionApplication.getAppContext().getContentResolver();
    }

    @Before
    public void beforeEachTest() {
        PollutionProvider.cleanAll(contentResolver);
    }

    @Test
    public void addedEventsAreCorrect() throws RemoteException, OperationApplicationException {

        String username = "maske94";
        String childId = "child1";

        /**
         * Store some events
         */
        ArrayList<Event> eventsToStore = new ArrayList<>();

        Event event1 = new Event(username, childId, "45.56", "2017-06-19T15:54:21", "45.89", "65.23");
        Event event2 = new Event(username, childId, "45.56", "2017-06-19T15:54:22", "45.89", "65.23");
        Event event3 = new Event(username, childId, "45.56", "2017-06-19T15:54:23", "45.89", "65.23");
        Event event4 = new Event(username, childId, "45.56", "2017-06-19T15:54:24", "45.89", "65.23");

        eventsToStore.add(event1);
        eventsToStore.add(event2);
        eventsToStore.add(event3);
        eventsToStore.add(event4);
        PollutionProvider.storeEvents(eventsToStore, contentResolver);

        /**
         * Read the stored events
         */
        ArrayList<Event> storedEvents = PollutionProvider.getEvents(username, childId, null, null, contentResolver);

        /**
         * Assertions
         */
        assertEquals(eventsToStore.size(), storedEvents.size());
        for (int i = 0; i < storedEvents.size(); i++) {
            assertEquals(eventsToStore.get(i), storedEvents.get(i));
        }

    }

    @Test
    public void getEventsDateStart() throws RemoteException, OperationApplicationException {
        String username = "maske94";
        String childId = "child1";

        /**
         * Store some events
         */
        ArrayList<Event> eventsToStore = new ArrayList<>();

        Event event1 = new Event(username, childId, "45.56", "2017-06-19T14:54:21", "45.89", "65.23");
        Event event2 = new Event(username, childId, "45.56", "2017-06-19T15:54:22", "45.89", "65.23");
        Event event3 = new Event(username, childId, "45.56", "2017-06-19T15:00:00", "45.89", "65.23");
        Event event4 = new Event(username, childId, "45.56", "2018-06-19T16:54:24", "45.89", "65.23");

        eventsToStore.add(event1);
        eventsToStore.add(event2);
        eventsToStore.add(event3);
        eventsToStore.add(event4);
        PollutionProvider.storeEvents(eventsToStore, contentResolver);

        /**
         * Read the stored events
         */
        ArrayList<Event> storedEvents = PollutionProvider.getEvents(username, childId, "2017-06-19T15:00:00", null, contentResolver);

        /**
         * Assertions
         */
        assertEquals(3, storedEvents.size()); // There are 3 events with timestamp >= 2017-06-19T15:00:00
        assertEquals(event2, storedEvents.get(0));
        assertEquals(event3, storedEvents.get(1));
        assertEquals(event4, storedEvents.get(2));
    }

    @Test
    public void getEventsDateEnd() throws RemoteException, OperationApplicationException {
        String username = "maske94";
        String childId = "child1";

        /**
         * Store some events
         */
        ArrayList<Event> eventsToStore = new ArrayList<>();

        Event event1 = new Event(username, childId, "45.56", "2017-06-19T14:54:21", "45.89", "65.23");
        Event event2 = new Event(username, childId, "45.56", "2017-06-19T00:00:00", "45.89", "65.23");
        Event event3 = new Event(username, childId, "45.56", "2017-01-01T00:00:00", "45.89", "65.23");
        Event event4 = new Event(username, childId, "45.56", "2015-06-19T16:54:24", "45.89", "65.23");

        eventsToStore.add(event1);
        eventsToStore.add(event2);
        eventsToStore.add(event3);
        eventsToStore.add(event4);
        PollutionProvider.storeEvents(eventsToStore, contentResolver);

        /**
         * Read the stored events
         */
        ArrayList<Event> storedEvents = PollutionProvider.getEvents(username, childId, null, "2017-01-01T00:00:00", contentResolver);

        /**
         * Assertions
         */
        assertEquals(2, storedEvents.size()); // There are 2 events with timestamp <= 2017-01-01T00:00:00
        assertEquals(event3, storedEvents.get(0));
        assertEquals(event4, storedEvents.get(1));
    }

    @Test
    public void getEventsDateStartDateEnd() throws RemoteException, OperationApplicationException {
        String username = "maske94";
        String childId = "child1";

        /**
         * Store some events
         */
        ArrayList<Event> eventsToStore = new ArrayList<>();

        Event event1 = new Event(username, childId, "45.56", "2014-06-19T14:54:21", "45.89", "65.23");
        Event event2 = new Event(username, childId, "45.56", "2017-06-19T00:00:00", "45.89", "65.23");
        Event event3 = new Event(username, childId, "45.56", "2017-01-01T00:00:00", "45.89", "65.23");
        Event event4 = new Event(username, childId, "45.56", "2015-06-19T16:54:24", "45.89", "65.23");

        eventsToStore.add(event1);
        eventsToStore.add(event2);
        eventsToStore.add(event3);
        eventsToStore.add(event4);
        PollutionProvider.storeEvents(eventsToStore, contentResolver);

        /**
         * Read the stored events
         */
        ArrayList<Event> storedEvents = PollutionProvider.getEvents(username, childId, "2014-01-01T00:00:00", "2014-12-31T23:59:59", contentResolver);

        /**
         * Assertions
         */
        assertEquals(1, storedEvents.size()); // There are 2 events with timestamp <= 2017-01-01T00:00:00
        assertEquals(event1, storedEvents.get(0));
    }

    @Test
    public void deleteEventsTest() throws RemoteException, OperationApplicationException {
        String username = "maske94";
        String childId = "child1";

        /**
         * Store some events
         */
        ArrayList<Event> eventsToStore = new ArrayList<>();

        Event event1 = new Event(username, childId, "45.56", "2014-06-19T14:54:21", "45.89", "65.23");
        Event event2 = new Event(username, childId, "45.56", "2017-06-19T00:00:00", "45.89", "65.23");
        Event event3 = new Event(username, childId, "45.56", "2017-01-01T00:00:00", "45.89", "65.23");
        Event event4 = new Event(username, childId, "45.56", "2015-06-19T16:54:24", "45.89", "65.23");

        eventsToStore.add(event1);
        eventsToStore.add(event2);
        eventsToStore.add(event3);
        eventsToStore.add(event4);
        ContentProviderResult[] results = PollutionProvider.storeEvents(eventsToStore, contentResolver);

        assertEquals(4,results.length);

        /**
         * Delete event3 and event4
         */
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(Integer.parseInt(results[2].uri.getLastPathSegment()));
        ids.add(Integer.parseInt(results[3].uri.getLastPathSegment()));
        PollutionProvider.deleteEvents(ids,contentResolver);

        /**
         * Read the remaining events
         */
        ArrayList<Event> storedEvents = PollutionProvider.getEvents(username, childId, null, null, contentResolver);

        /**
         * Assertions
         */
        assertEquals(2, storedEvents.size());
        assertEquals(event1,storedEvents.get(0));
        assertEquals(event2,storedEvents.get(1));
    }

}
