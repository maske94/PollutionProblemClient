package com.supsi.alessandro.pollutionproblemclient.api.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alessandro on 01/05/2017.
 */

public class Event {
    @SerializedName("_id")
    private String eventId;

    @SerializedName("username")
    private String username;

    @SerializedName("childId")
    private String childId;

    @SerializedName("pollutionValue")
    private String pollutionValue;

    @SerializedName("timeStamp")
    private String timeStamp;

    @SerializedName("gpsLat")
    private String gpsLat;

    @SerializedName("gpsLong")
    private String gpsLong;

    public Event(String username, String childId, String pollutionValue, String timeStamp, String gpsLat, String gpsLong) {
        this.username = username;
        this.childId = childId;
        this.pollutionValue = pollutionValue;
        this.timeStamp = timeStamp;
        this.gpsLat = gpsLat;
        this.gpsLong = gpsLong;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getPollutionValue() {
        return pollutionValue;
    }

    public void setPollutionValue(String pollutionValue) {
        this.pollutionValue = pollutionValue;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(String gpsLat) {
        this.gpsLat = gpsLat;
    }

    public String getGpsLong() {
        return gpsLong;
    }

    public void setGpsLong(String gpsLong) {
        this.gpsLong = gpsLong;
    }
}
