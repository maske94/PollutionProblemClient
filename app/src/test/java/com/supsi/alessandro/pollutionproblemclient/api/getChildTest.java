package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.GeneralResponse;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Alessandro on 01/05/2017.
 */
public class getChildTest {

    GeneralResponse<Child> res = null;

    @Before
    public void setUp() throws IOException {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call call = apiInterface.getChild("paperino", "58e656ce3b37a828401f4183");
        res = (GeneralResponse<Child>) call.execute().body();
    }

    @Test
    public void childIsCorrect() throws Exception {
        Child child = res.getBody();
        // expected,actual
        assertEquals("luca2",child.getLastName());
        assertEquals("masche",child.getFirstName());
        assertEquals("asfw5twrvsdgf5",child.getDeviceId());
        assertEquals("58e656ce3b37a828401f4183",child.getChildId());
        assertEquals("1994-05-03T00:00:00.000Z",child.getBirthDate());
    }
}
