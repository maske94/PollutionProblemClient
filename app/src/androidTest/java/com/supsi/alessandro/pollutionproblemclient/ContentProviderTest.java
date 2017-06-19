package com.supsi.alessandro.pollutionproblemclient;

import android.content.ContentProvider;
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
    public static void setUp(){
        contentResolver = PollutionApplication.getAppContext().getContentResolver();
    }

    @Before
    public void beforeEachTest(){
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

        Event event1 = new Event(username,childId,"45.56","19-06-2017T15:54:21","45.89","65.23");
        Event event2 = new Event(username,childId,"45.56","19-06-2017T15:54:22","45.89","65.23");
        Event event3 = new Event(username,childId,"45.56","19-06-2017T15:54:23","45.89","65.23");
        Event event4 = new Event(username,childId,"45.56","19-06-2017T15:54:24","45.89","65.23");

        eventsToStore.add(event1);
        eventsToStore.add(event2);
        eventsToStore.add(event3);
        eventsToStore.add(event4);
        PollutionProvider.storeEvents(eventsToStore,contentResolver);

        /**
         * Read the stored events
         */
        ArrayList<Event> storedEvents = PollutionProvider.getEvents(username,childId,null,null,contentResolver);

        /**
         * Assertions
         */
        assertEquals(eventsToStore.size(),storedEvents.size());
        for (int i = 0; i < storedEvents.size(); i++) {
            assertEquals(eventsToStore.get(i),storedEvents.get(i));
        }

    }

    @Test
    public void timestampBoundsAreCorrect() throws RemoteException, OperationApplicationException {
        String username = "maske94";
        String childId = "child1";

        /**
         * Store some events
         */
        ArrayList<Event> eventsToStore = new ArrayList<>();

        Event event1 = new Event(username,childId,"45.56","19-06-2017T14:54:21","45.89","65.23");
        Event event2 = new Event(username,childId,"45.56","19-06-2017T15:54:22","45.89","65.23");
        Event event3 = new Event(username,childId,"45.56","19-06-2017T15:54:23","45.89","65.23");
        Event event4 = new Event(username,childId,"45.56","19-06-2017T16:54:24","45.89","65.23");

        eventsToStore.add(event1);
        eventsToStore.add(event2);
        eventsToStore.add(event3);
        eventsToStore.add(event4);
        PollutionProvider.storeEvents(eventsToStore,contentResolver);

        /**
         * Read the stored events
         */
        ArrayList<Event> storedEvents = PollutionProvider.getEvents(username,childId,"19-06-2017T15:00:00",null,contentResolver);

        /**
         * Assertions
         */
        assertEquals(3,storedEvents.size()); // There are 3 events with timestamp > 19-06-2017T15:00:00
        assertEquals(event2,storedEvents.get(0));
        assertEquals(event3,storedEvents.get(1));
        assertEquals(event4,storedEvents.get(2));

    }
}
