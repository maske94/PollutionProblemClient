package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.ChildrenList;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Alessandro on 01/05/2017.
 */
interface APIInterface {

    @POST("/api/addUser")
    Call<User> addUser(@Body User user);

    @POST("/api/addChild")
    Call<Child> addChild(@Body Child child);

    @POST("/api/addEvent")
    Call<Event> addEvent(@Body Event event);

    @GET("/api/getChild?")
    Call<Child> getChild(@Query("username") String parentUsername, @Query("childId") String childId);

    @POST("/api/getChildren?")
    Call<ChildrenList> getChildrenList(@Query("username") String parentUsername);
}
