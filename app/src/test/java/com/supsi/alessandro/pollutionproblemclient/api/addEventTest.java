package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.GeneralResponse;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.User;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by Alessandro on 13/05/2017.
 */

public class addEventTest {

    private static GeneralResponse<Event> response = null;
    private static APIInterface apiInterface = null;
    private static final String TEST_USERNAME = "addEventTest";
    private String childId;

    @BeforeClass
    public static void setUp() throws IOException {
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Before
    public void beforeEachTest() throws IOException {
        apiInterface.removeUser(TEST_USERNAME).execute();
        apiInterface.addUser(new User(TEST_USERNAME, "pass", "Alessandro", "Mascheroni", "1994-05-04", "Dongo")).execute();
        Child child =  apiInterface.addChild(new Child(TEST_USERNAME,"Richard","sdfv")).execute().body().getBody();
        childId = child.getChildId();
    }

    @Test
    public void addedEventIsCorrect() throws IOException {
        Event eventToAdd = new Event(TEST_USERNAME,childId,"137","2017-05-13T15:00:00.003Z","123.34","45.32");
        response = apiInterface.addEvent(eventToAdd).execute().body();

        Event addedEvent = response.getBody();

        assertEquals(APIConstants.SUCCESS_EVENT_ADDED,response.getMessage());
        assertEquals(eventToAdd.getGpsLat(),addedEvent.getGpsLat());
        assertEquals(eventToAdd.getGpsLong(),addedEvent.getGpsLong());
        assertEquals(eventToAdd.getPollutionValue(),addedEvent.getPollutionValue());
        assertEquals(eventToAdd.getTimeStamp(),addedEvent.getTimeStamp());
        assertEquals(eventToAdd.getUsername(),addedEvent.getUsername());
        assertEquals(eventToAdd.getChildId(),addedEvent.getChildId());

        assertNotNull(addedEvent.getEventId());
    }

    @Test
    public void parentUsernameIsNull() throws IOException {
        Event eventToAdd = new Event(null,childId,"137","2017-05-13T15:00:00.003Z","123.34","45.32");
        response = apiInterface.addEvent(eventToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_USERNAME, response.getError());
    }

    @Test
    public void parentUsernameDoesNotExist() throws IOException {
        Event eventToAdd = new Event("notExist",childId,"137","2017-05-13T15:00:00.003Z","123.34","45.32");
        response = apiInterface.addEvent(eventToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_USERNAME_NOT_EXIST, response.getError());
    }

    @Test
    public void childIdDoesNotExist() throws IOException {
        Event eventToAdd = new Event(TEST_USERNAME,"childIdNotExist","137","2017-05-13T15:00:00.003Z","123.34","45.32");
        response = apiInterface.addEvent(eventToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_CHILDID_NOT_EXIST+"'"+TEST_USERNAME+"'", response.getError());
    }

    @Test
    public void wrongTimeStampFormat() throws IOException {
        Event eventToAdd = new Event(TEST_USERNAME,"childIdNotExist","137","wrongTimeStamp","123.34","45.32");
        response = apiInterface.addEvent(eventToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_INVALID_FIELD_TIMESTAMP, response.getError());
    }

    @Test
    public void childIdIsNull() throws IOException {
        Event eventToAdd = new Event(TEST_USERNAME,null,"137","2017-05-13T15:00:00.003Z","123.34","45.32");
        response = apiInterface.addEvent(eventToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_CHILDID, response.getError());
    }

    @Test
    public void pollValueIsNull() throws IOException {
        Event eventToAdd = new Event(TEST_USERNAME,childId,null,"2017-05-13T15:00:00.003Z","123.34","45.32");
        response = apiInterface.addEvent(eventToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_POLLVALUE, response.getError());
    }

    @Test
    public void timeStampIsNull() throws IOException {
        Event eventToAdd = new Event(TEST_USERNAME,childId,"137",null,"123.34","45.32");
        response = apiInterface.addEvent(eventToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_TIMESTAMP, response.getError());
    }

    @Test
    public void gpsLatIsNull() throws IOException {
        Event eventToAdd = new Event(TEST_USERNAME,childId,"137","2017-05-13T15:00:00.003Z",null,"45.32");
        response = apiInterface.addEvent(eventToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_GPSLAT, response.getError());
    }

    @Test
    public void gpsLongIsNull() throws IOException {
        Event eventToAdd = new Event(TEST_USERNAME,childId,"137","2017-05-13T15:00:00.003Z","123.34",null);
        response = apiInterface.addEvent(eventToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_GPSLONG, response.getError());
    }











}
