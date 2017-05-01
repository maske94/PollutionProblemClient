package com.supsi.alessandro.pollutionproblemclient.api;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Alessandro on 01/05/2017.
 *
 */
public class addUserTest {
    @Test
    public void getChild() throws Exception {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call call = apiInterface.getChild("paperino", "58e656ce3b37a828401f4183");

        Response res = call.execute();
        System.out.println(res.body());
    }

    @Test
    public void getChildren() throws Exception {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call call = apiInterface.getChildrenList("paperino");

        Response res = call.execute();
        System.out.println(res.body());
    }
}
