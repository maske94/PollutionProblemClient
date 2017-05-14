package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.GeneralResponse;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.User;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Alessandro on 14/05/2017.
 */

public class deleteUserTest {

    private static GeneralResponse<User> response = null;
    private static APIInterface apiInterface = null;
    private static final String TEST_USERNAME = "deleteUserTest";

    @BeforeClass
    public static void setUp() throws IOException {
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Test
    public void deletedUserIsCorrect() throws IOException, ParseException {
        User userToDelete = new User(TEST_USERNAME, "pass", "Alessandro", "Mascheroni", "1994-05-04", "Dongo");
        apiInterface.addUser(userToDelete).execute();

        response = apiInterface.deleteUser(userToDelete.getUsername()).execute().body();
        User deletedUser = response.getBody();

        assertEquals(APIConstants.SUCCESS_USER_REMOVED, response.getMessage());
        assertEquals(userToDelete.getUsername(), deletedUser.getUsername());
        assertEquals(userToDelete.getFirstName(), deletedUser.getFirstName());
        assertEquals(userToDelete.getLastName(), deletedUser.getLastName());
        assertEquals(userToDelete.getCity(), deletedUser.getCity());
        assertEquals(userToDelete.getPassword(), deletedUser.getPassword());

        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD", Locale.ITALY);
        Date date1 = dateFormat.parse(userToDelete.getBirthDate());
        Date date2 = dateFormat.parse(deletedUser.getBirthDate());

        assertTrue(date1.compareTo(date2) == 0);
    }

    @Test
    public void missedUsernameFiled() throws IOException {
        response = apiInterface.deleteUser(null).execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_USERNAME, response.getError());
    }

    @Test
    public void parentUsernameDoesNotExist() throws IOException {
        response = apiInterface.deleteUser("usernameThatDoesNotExist").execute().body();

        assertNull(response.getBody());
        assertNull(response.getMessage());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_USERNAME_NOT_EXIST, response.getError());
    }


}
