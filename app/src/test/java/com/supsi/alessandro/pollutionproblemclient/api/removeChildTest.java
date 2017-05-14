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
 * Created by Alessandro on 14/05/2017.
 */

public class removeChildTest {

    private static GeneralResponse<Child> response = null;
    private static APIInterface apiInterface = null;
    private static final String TEST_USERNAME = "removeChildTest";

    @BeforeClass
    public static void setUp() throws IOException {
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Before
    public void beforeEachTest() throws IOException {
        apiInterface.addUser(new User(TEST_USERNAME, "pass", "Alessandro", "Mascheroni", "1994-05-04", "Dongo")).execute();
    }

    @Test
    public void removedChildIsCorrect() throws IOException {
        // Init
        Child childToRemove = new Child(TEST_USERNAME, "Bernard", "2135423hygr7t6");
        childToRemove = apiInterface.addChild(childToRemove).execute().body().getBody();
        String childId = childToRemove.getChildId();

        // Real test
        response = apiInterface.removeChild(TEST_USERNAME, childId).execute().body();
        Child removedChild = response.getBody();

        assertEquals(APIConstants.SUCCESS_CHILD_REMOVED + "'" + TEST_USERNAME + "'", response.getMessage());
        assertEquals(childToRemove.getChildId(), removedChild.getChildId());
        assertEquals(childToRemove.getParentUsername(), removedChild.getParentUsername());
        assertEquals(childToRemove.getFirstName(), removedChild.getFirstName());
        assertEquals(childToRemove.getLastName(), removedChild.getLastName());
        assertEquals(childToRemove.getBirthDate(), removedChild.getBirthDate());
        assertEquals(childToRemove.getDeviceId(), removedChild.getDeviceId());
    }

    @Test
    public void missedUsernameField() throws IOException {
        response = apiInterface.removeChild(null, "fuffa").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_USERNAME, response.getError());
    }

    @Test
    public void missedChildIdField() throws IOException {
        response = apiInterface.removeChild(TEST_USERNAME, null).execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_CHILDID, response.getError());
    }

    @Test
    public void parentUsernameDoesNotExist() throws IOException {
        response = apiInterface.removeChild("usernameThatDoesNotExist", "fuffa").execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_USERNAME_NOT_EXIST, response.getError());
    }

    @Test
    public void childIdDoesNotExist() throws IOException {
        response = apiInterface.removeChild(TEST_USERNAME, "childIdThatDoesNotExist").execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_CHILDID_NOT_EXIST+"'"+TEST_USERNAME+"'", response.getError());
    }

}
