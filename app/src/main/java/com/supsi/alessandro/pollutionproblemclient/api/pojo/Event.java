package com.supsi.alessandro.pollutionproblemclient.api.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alessandro on 01/05/2017.
 *
 */
public class Event{
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

    private int synced;

    public Event(String username, String childId, String pollutionValue, String timeStamp, String gpsLat, String gpsLong) {
        this.username = username;
        this.childId = childId;
        this.pollutionValue = pollutionValue;
        this.timeStamp = timeStamp;
        this.gpsLat = gpsLat;
        this.gpsLong = gpsLong;
    }

    public Event(String username, String childId, float pollValue, String timestamp, float gpsLat, float gpsLong) {
        this(username,childId,pollValue+"",timestamp,gpsLat+"",gpsLong+"");
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId+"";
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

    public void setSynced(int synced) {
        this.synced = synced;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", username='" + username + '\'' +
                ", childId='" + childId + '\'' +
                ", pollutionValue='" + pollutionValue + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", gpsLat='" + gpsLat + '\'' +
                ", gpsLong='" + gpsLong + '\'' +
                ", synced=" + synced +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (username != null ? !username.equals(event.username) : event.username != null)
            return false;
        if (childId != null ? !childId.equals(event.childId) : event.childId != null) return false;
        if (pollutionValue != null ? !pollutionValue.equals(event.pollutionValue) : event.pollutionValue != null)
            return false;
        if (timeStamp != null ? !timeStamp.equals(event.timeStamp) : event.timeStamp != null)
            return false;
        if (gpsLat != null ? !gpsLat.equals(event.gpsLat) : event.gpsLat != null) return false;
        return gpsLong != null ? gpsLong.equals(event.gpsLong) : event.gpsLong == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (childId != null ? childId.hashCode() : 0);
        result = 31 * result + (pollutionValue != null ? pollutionValue.hashCode() : 0);
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        result = 31 * result + (gpsLat != null ? gpsLat.hashCode() : 0);
        result = 31 * result + (gpsLong != null ? gpsLong.hashCode() : 0);
        return result;
    }
}
