package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.GeneralResponse;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.User;

import org.junit.Before;
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
 * Created by Alessandro on 07/05/2017.
 *
 */
public class addUserTest {

    private GeneralResponse<User> response = null;
    private APIInterface apiInterface = null;

    @Before
    public void setUp() throws IOException {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        //TODO remove username maske94 before run these tests
    }

    @Test
    public void addedUserIsCorrect() throws IOException, ParseException {
        User userToAdd = new User("maske94", "pass", "Alessandro", "Mascheroni", "1994-05-04", "Dongo");
        response = apiInterface.addUser(userToAdd).execute().body();
        User addedUser = response.getBody();

        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD", Locale.ITALY);
        Date date1 = dateFormat.parse(userToAdd.getBirthDate());
        Date date2 = dateFormat.parse(addedUser.getBirthDate());

        assertTrue(date1.compareTo(date2) == 0);

        assertEquals(userToAdd.getUsername(), addedUser.getUsername());
        assertEquals(userToAdd.getPassword(), addedUser.getPassword());
        assertEquals(userToAdd.getFirstName(), addedUser.getFirstName());
        assertEquals(userToAdd.getLastName(), addedUser.getLastName());
        assertEquals(userToAdd.getCity(), addedUser.getCity());

        assertEquals(0, addedUser.getChildren().size());
    }

    @Test
    public void usernameAlreadyExist() throws IOException {
        User userToAdd = new User("maske94", "pass", "Alessandro", "Mascheroni", "1994-05-04", "Dongo");
        response = apiInterface.addUser(userToAdd).execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_USERNAME_ALREADY_EXISTS, response.getError());
    }

    @Test
    public void invalidBirthDateFormat() throws IOException {
        User userToAdd = new User("maske94", "pass", "Alessandro", "Mascheroni", "invalidDate", "Dongo");
        response = apiInterface.addUser(userToAdd).execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_INVALID_FIELD_BIRTHDATE, response.getError());
    }

    @Test
    public void missedUsernameFiled() throws IOException {
        User userToAdd = new User(null, "pass", "Alessandro", "Mascheroni", "invalidDate", "Dongo");
        response = apiInterface.addUser(userToAdd).execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_USERNAME, response.getError());
    }

    @Test
    public void missedPasswordFiled() throws IOException {
        User userToAdd = new User("newUser", null, "Alessandro", "Mascheroni", "invalidDate", "Dongo");
        response = apiInterface.addUser(userToAdd).execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals(APIConstants.ERROR_MISSING_FIELD_PASSWORD, response.getError());
    }

}
