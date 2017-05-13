package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
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
 * <p>
 * Tests for the /api/addChild API call.
 */

public class addChildTest {

    private static APIInterface apiInterface = null;
    private static GeneralResponse<Child> response = null;
    private static final String TEST_USERNAME = "addChildTestUsername";

    @BeforeClass
    public static void setUp() throws IOException {
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Before
    public void beforeEachTest() throws IOException {
        // This two calls ensure that next tests start always from the same state
        // I.E. a user without any children associated
        apiInterface.deleteUser(TEST_USERNAME).execute();
        apiInterface.addUser(new User(TEST_USERNAME, "pass", "Alessandro", "Mascheroni", "1994-05-04", "Dongo")).execute();
    }

    @Test
    public void addChildIsCorrect() throws IOException {

        Child childToAdd = new Child(TEST_USERNAME, "Bernard", "asdkfjshb");
        response = apiInterface.addChild(childToAdd).execute().body();
        Child addedChild = response.getBody();

        assertEquals(childToAdd.getFirstName(), addedChild.getFirstName());
        assertEquals(childToAdd.getDeviceId(), addedChild.getDeviceId());
        assertNull(addedChild.getBirthDate());
        assertNull(addedChild.getLastName());
        assertNotNull(addedChild.getChildId());
    }

    @Test
    public void parentUsernameIsNull() throws IOException {
        Child childToAdd = new Child(null, "sdfgs", "sdfs");
        response = apiInterface.addChild(childToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_USERNAME, response.getError());
    }

    @Test
    public void parentUsernameDoesNotExist() throws IOException {
        Child childToAdd = new Child("fuffafuffa", "sdfgs", "sdfs");
        response = apiInterface.addChild(childToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_USERNAME_NOT_EXIST, response.getError());
    }

    @Test
    public void deviceIdIsNull() throws IOException {
        Child childToAdd = new Child(TEST_USERNAME, "sdfgs", null);
        response = apiInterface.addChild(childToAdd).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_DEVICEID, response.getError());
    }

    @Test
    public void deviceAlreadyPaired() throws IOException {

        final String deviceId = "1234";

        Child child1 = new Child(TEST_USERNAME, "Bernard", deviceId);
        Child child2 = new Child(TEST_USERNAME, "Alessandro", deviceId);

        response = apiInterface.addChild(child1).execute().body();
        response = apiInterface.addChild(child2).execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_DEVICE_ALREADY_PAIRED, response.getError());
    }
}
