package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.GeneralResponse;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by Alessandro on 07/05/2017.
 * <p>
 * This class tests the /api/getChildren API call.
 */

public class getChildrenTest {

    private GeneralResponse<List<Child>> response = null;
    private APIInterface apiInterface = null;

    @Before
    public void setUp() throws IOException {
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Test
    public void childrenAreCorrect() throws IOException {
        response = apiInterface.getChildrenList("paperino").execute().body();
        List<Child> children = response.getBody();

        assertEquals(3, children.size());
        assertEquals("asfw5twrvsdgf5", children.get(0).getDeviceId());
        assertEquals("asfw5twrvsdgf5j", children.get(1).getDeviceId());
        assertEquals("asfw5twrvsdgf555ff", children.get(2).getDeviceId());

        for (Child c : children) { assertEquals("1994-05-03T00:00:00.000Z", c.getBirthDate());}
    }

    @Test
    public void noChildrenForGivenParentUsername() throws IOException {
        response = apiInterface.getChildrenList("testNoChildren").execute().body();
        List<Child> children = response.getBody();

        assertEquals(0,children.size());
    }

    @Test
    public void parentUsernameDoesNotExist() throws IOException {
        response = apiInterface.getChildrenList("doesNotExist").execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals("The given username does not exist", response.getError());
    }

    @Test
    public void parentUsernameIsNull() throws Exception {
        // Send to the API a null parent username
        response = apiInterface.getChildrenList(null).execute().body();

        assertNull(response.getBody());
        assertNotNull(response.getError());
        assertEquals("Missed mandatory 'username' field in the request", response.getError());
    }
}
