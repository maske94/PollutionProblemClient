package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.GeneralResponse;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.User;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by Alessandro on 18/05/2017.
 */

public class getEventsTest {
    private GeneralResponse<List<Event>> response = null;
    private static APIInterface apiInterface = null;
    private static final String TEST_USERNAME = "getEventsTest";
    private Child addedChild;

    @BeforeClass
    public static void setUp() throws IOException {
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Before
    public void beforeEachTest() throws IOException {
        User userToAdd = new User(TEST_USERNAME, "pass", "Alessandro", "Mascheroni", "1994-05-04", "Dongo");
        Child childToAdd = new Child(TEST_USERNAME, "Bernard", "alskfahsdfiauy98a7");

        apiInterface.removeUser(TEST_USERNAME).execute();
        apiInterface.addUser(userToAdd).execute();
        addedChild = apiInterface.addChild(childToAdd).execute().body().getBody();
    }

    @Test
    public void getEventsIsCorrect() throws IOException {
        Event event1 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2016-05-13T15:00:00.003Z", "14.23", "45.78");
        Event event2 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:00:00.003Z", "14.23", "45.78");

        apiInterface.addEvent(event1).execute();
        apiInterface.addEvent(event2).execute();

        response = apiInterface.getEvents(TEST_USERNAME, addedChild.getChildId(), "2017-05-18T00:00:00.000Z", "2017-05-18T23:59:59.999Z").execute().body();

        assertNull(response.getError());
        assertNotNull(response.getBody());
        assertNotNull(response.getMessage());

        List<Event> events = response.getBody();

        assertEquals(1, events.size());

        Event actualEvent = events.get(0);

        assertEquals(event2.getPollutionValue(), actualEvent.getPollutionValue());
        assertEquals(event2.getGpsLat(), actualEvent.getGpsLat());
        assertEquals(event2.getGpsLong(), actualEvent.getGpsLong());
        assertEquals(event2.getTimeStamp(), actualEvent.getTimeStamp());
    }

    @Test
    public void getEventsIsCorrect2() throws IOException {
        Event event1 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:01:00.000Z", "14.23", "45.78");
        Event event2 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:02:00.000Z", "14.23", "45.78");
        Event event3 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:03:00.000Z", "14.23", "45.78");

        apiInterface.addEvent(event1).execute();
        apiInterface.addEvent(event2).execute();
        apiInterface.addEvent(event3).execute();

        response = apiInterface.getEvents(TEST_USERNAME, addedChild.getChildId(), "2017-05-18T00:00:00.000Z", "2017-05-18T23:59:59.999Z").execute().body();

        assertNull(response.getError());
        assertNotNull(response.getBody());
        assertNotNull(response.getMessage());

        List<Event> events = response.getBody();

        assertEquals(3, events.size());

        Event actualEvent1 = events.get(0);
        Event actualEvent2 = events.get(1);
        Event actualEvent3 = events.get(2);

        assertEquals(event1.getPollutionValue(), actualEvent1.getPollutionValue());
        assertEquals(event1.getGpsLat(), actualEvent1.getGpsLat());
        assertEquals(event1.getGpsLong(), actualEvent1.getGpsLong());
        assertEquals(event1.getTimeStamp(), actualEvent1.getTimeStamp());

        assertEquals(event2.getPollutionValue(), actualEvent2.getPollutionValue());
        assertEquals(event2.getGpsLat(), actualEvent2.getGpsLat());
        assertEquals(event2.getGpsLong(), actualEvent2.getGpsLong());
        assertEquals(event2.getTimeStamp(), actualEvent2.getTimeStamp());

        assertEquals(event3.getPollutionValue(), actualEvent3.getPollutionValue());
        assertEquals(event3.getGpsLat(), actualEvent3.getGpsLat());
        assertEquals(event3.getGpsLong(), actualEvent3.getGpsLong());
        assertEquals(event3.getTimeStamp(), actualEvent3.getTimeStamp());
    }

    @Test
    public void getEventsIsCorrect3() throws IOException {
        Event event1 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:01:00.000Z", "14.23", "45.78");
        Event event2 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:02:00.000Z", "14.23", "45.78");
        Event event3 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:03:00.000Z", "14.23", "45.78");
        Event event4 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:04:00.000Z", "14.23", "45.78");
        Event event5 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:05:00.000Z", "14.23", "45.78");
        Event event6 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:06:00.000Z", "14.23", "45.78");
        Event event7 = new Event(TEST_USERNAME, addedChild.getChildId(), "137", "2017-05-18T15:07:00.000Z", "14.23", "45.78");

        apiInterface.addEvent(event1).execute();
        apiInterface.addEvent(event2).execute();
        apiInterface.addEvent(event3).execute();
        apiInterface.addEvent(event4).execute();
        apiInterface.addEvent(event5).execute();
        apiInterface.addEvent(event6).execute();
        apiInterface.addEvent(event7).execute();

        response = apiInterface.getEvents(TEST_USERNAME, addedChild.getChildId(), "2017-05-18T15:00:00.000Z", "2017-05-18T15:05:00.000Z").execute().body();

        assertNull(response.getError());
        assertNotNull(response.getBody());
        assertNotNull(response.getMessage());

        List<Event> events = response.getBody();

        assertEquals(5, events.size());

        Event actualEvent1 = events.get(0);
        Event actualEvent2 = events.get(1);
        Event actualEvent3 = events.get(2);
        Event actualEvent4 = events.get(3);
        Event actualEvent5 = events.get(4);

        assertEquals(event1.getPollutionValue(), actualEvent1.getPollutionValue());
        assertEquals(event1.getGpsLat(), actualEvent1.getGpsLat());
        assertEquals(event1.getGpsLong(), actualEvent1.getGpsLong());
        assertEquals(event1.getTimeStamp(), actualEvent1.getTimeStamp());

        assertEquals(event2.getPollutionValue(), actualEvent2.getPollutionValue());
        assertEquals(event2.getGpsLat(), actualEvent2.getGpsLat());
        assertEquals(event2.getGpsLong(), actualEvent2.getGpsLong());
        assertEquals(event2.getTimeStamp(), actualEvent2.getTimeStamp());

        assertEquals(event3.getPollutionValue(), actualEvent3.getPollutionValue());
        assertEquals(event3.getGpsLat(), actualEvent3.getGpsLat());
        assertEquals(event3.getGpsLong(), actualEvent3.getGpsLong());
        assertEquals(event3.getTimeStamp(), actualEvent3.getTimeStamp());

        assertEquals(event4.getPollutionValue(), actualEvent4.getPollutionValue());
        assertEquals(event4.getGpsLat(), actualEvent4.getGpsLat());
        assertEquals(event4.getGpsLong(), actualEvent4.getGpsLong());
        assertEquals(event4.getTimeStamp(), actualEvent4.getTimeStamp());

        assertEquals(event5.getPollutionValue(), actualEvent5.getPollutionValue());
        assertEquals(event5.getGpsLat(), actualEvent5.getGpsLat());
        assertEquals(event5.getGpsLong(), actualEvent5.getGpsLong());
        assertEquals(event5.getTimeStamp(), actualEvent5.getTimeStamp());
    }

    @Test
    public void noEventsForGivenChildId() throws IOException {
        response = apiInterface.getEvents(TEST_USERNAME,addedChild.getChildId(),"2017-05-18T15:00:00.000Z", "2017-05-18T15:05:00.000Z").execute().body();

        assertNull(response.getError());
        assertNotNull(response.getBody());
        assertNotNull(response.getMessage());
        assertEquals(APIConstants.SUCCESS_GENERAL,response.getMessage());

        List<Event> events = response.getBody();
        assertEquals(0,events.size());
    }

    @Test
    public void parentUsernameDoesNotExist() throws IOException {
        response = apiInterface.getEvents("notExistUsername",addedChild.getChildId(),"2017-05-18T15:00:00.000Z", "2017-05-18T15:05:00.000Z").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_USERNAME_NOT_EXIST, response.getError());
    }

    @Test
    public void parentUsernameIsNull() throws Exception {
        response = apiInterface.getEvents(null,addedChild.getChildId(),"2017-05-18T15:00:00.000Z", "2017-05-18T15:05:00.000Z").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_USERNAME, response.getError());
    }

    @Test
    public void childIdIsNull() throws Exception {
        response = apiInterface.getEvents(TEST_USERNAME,null,"2017-05-18T15:00:00.000Z", "2017-05-18T15:05:00.000Z").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_CHILDID, response.getError());
    }

    @Test
    public void dateStartIsNull() throws Exception {
        response = apiInterface.getEvents(TEST_USERNAME,addedChild.getChildId(),null, "2017-05-18T15:05:00.000Z").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_DATE_START, response.getError());
    }

    @Test
    public void dateEndIsNull() throws Exception {
        response = apiInterface.getEvents(TEST_USERNAME,addedChild.getChildId(), "2017-05-18T15:05:00.000Z", null).execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_DATE_END, response.getError());
    }

    @Test
    public void dateStartNotValid() throws Exception {
        response = apiInterface.getEvents(TEST_USERNAME,addedChild.getChildId(),"2017-0515:05:00.000Z", "2017-05-18T15:05:00.000Z").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_INVALID_FIELD_DATE_START, response.getError());
    }

    @Test
    public void dateSEndNotValid() throws Exception {
        response = apiInterface.getEvents(TEST_USERNAME,addedChild.getChildId(),"2017-05-18T15:05:00.000Z", "2017-05-18ciao").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_INVALID_FIELD_DATE_END, response.getError());
    }

    @Test
    public void childIdDoesNotExist() throws Exception {
        response = apiInterface.getEvents(TEST_USERNAME,"idThatNotExist","2017-05-18T15:05:00.000Z", "2017-05-18T15:05:00.000Z").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_CHILDID_NOT_EXIST + "'" + TEST_USERNAME + "'", response.getError());
    }


}
