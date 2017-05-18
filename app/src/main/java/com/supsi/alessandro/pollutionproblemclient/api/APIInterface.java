package com.supsi.alessandro.pollutionproblemclient.api;

import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Event;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.GeneralResponse;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Alessandro on 01/05/2017.
 * <p>
 * This interface contains methods necessary to perform requests towards our API
 */
interface APIInterface {
    @POST("/api/addUser")
    Call<GeneralResponse<User>> addUser(@Body User user);

    @POST("/api/addChild")
    Call<GeneralResponse<Child>> addChild(@Body Child child);

    @POST("/api/addEvent")
    Call<GeneralResponse<Event>> addEvent(@Body Event event);

    @GET("/api/getChild?")
    Call<GeneralResponse<Child>> getChild(@Query("username") String parentUsername, @Query("childId") String childId);

    @GET("/api/getChildren?")
    Call<GeneralResponse<List<Child>>> getChildrenList(@Query("username") String parentUsername);

    @GET("/api/getEvents")
    Call<GeneralResponse<List<Event>>> getEvents(@Query("username") String parentUsername,@Query("childId") String childId,@Query("dateStart") String dateStart,@Query("dateEnd") String dateEnd);

    @DELETE("/api/removeChild")
    Call<GeneralResponse<Child>> removeChild(@Query("username") String parentUsername, @Query("childId") String childId);

    @DELETE("/api/removeUser")
    Call<GeneralResponse<User>> removeUser(@Query("username") String parentUsername);

}
