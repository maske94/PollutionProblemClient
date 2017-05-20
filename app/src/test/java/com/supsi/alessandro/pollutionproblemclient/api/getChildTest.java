package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.GeneralResponse;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by Alessandro on 01/05/2017.
 * <p>
 * This class tests the /api/getChild API call.
 */
public class getChildTest {

    private GeneralResponse<Child> response = null;
    private static APIInterface apiInterface = null;

    @BeforeClass
    public static void setUp() throws IOException {
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Test
    public void childIsCorrect() throws Exception {
        response = apiInterface.getChild("paperino", "58e656ce3b37a828401f4183").execute().body();

        Child child = response.getBody();
        // expected,actual
        assertEquals("luca2", child.getLastName());
        assertEquals("masche", child.getFirstName());
        assertEquals("asfw5twrvsdgf5", child.getDeviceId());
        assertEquals("58e656ce3b37a828401f4183", child.getChildId());
        assertEquals("1994-05-03T00:00:00.000Z", child.getBirthDate());
        assertEquals(APIConstants.SUCCESS_GENERAL, response.getMessage());
    }

    @Test
    public void parentUsernameIsNull() throws Exception {
        // Send to the API a null parent username
        response = apiInterface.getChild(null, "58e656ce3b37a828401f4183").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_USERNAME, response.getError());
    }

    @Test
    public void parentUsernameDoesNotExist() throws Exception {
        // Send to the API a parent username that does not exist
        response = apiInterface.getChild("fuffa", "58e656ce3b37a828401f4183").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_USERNAME_NOT_EXIST, response.getError());
    }

    @Test
    public void childIdDoesNotExist() throws Exception {
        // Send to the API a parent username that does not exist
        String parentUsername = "paperino";
        response = apiInterface.getChild(parentUsername, "idthatdoesnotexist").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_CHILDID_NOT_EXIST + "'" + parentUsername + "'", response.getError());
    }
}
