package com.supsi.alessandro.pollutionproblemclient.api.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alessandro on 01/05/2017.
 */
public class Child{

    @SerializedName("username")
    private String parentUsername;

    @SerializedName("_id")
    private String childId;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("birthDate")
    private String birthDate;

    @SerializedName("deviceId")
    private String deviceId;

    public Child(String parentUsername, String firstName, String deviceId) {
        this.parentUsername = parentUsername;
        this.firstName = firstName;
        this.deviceId = deviceId;
    }

    public String getParentUsername() {
        return parentUsername;
    }

    public void setParentUsername(String parentUsername) {
        this.parentUsername = parentUsername;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "Child{" +
                "childId='" + childId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Child child = (Child) o;

        return childId.equals(child.childId);

    }

    @Override
    public int hashCode() {
        return childId.hashCode();
    }
}
