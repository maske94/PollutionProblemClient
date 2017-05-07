package com.supsi.alessandro.pollutionproblemclient.api;

/**
 * Created by Alessandro on 07/05/2017.
 *
 */
class APIConstants {

    static final String API_BASE_URL = "http://localhost:3000";

    // Errors
    static final String ERROR_MISSING_FIELD_USERNAME = "Missed mandatory 'username' field in the request";
    static final String ERROR_MISSING_FIELD_PASSWORD = "Missed mandatory 'password' field in the request";
    static final String ERROR_MISSING_FIELD_DEVICEID = "Missed mandatory 'deviceId' field in the request";
    static final String ERROR_MISSING_FIELD_CHILDID = "Missed mandatory 'childId' field in the request";
    static final String ERROR_MISSING_FIELD_POLLVALUE = "Missed mandatory 'pollutionValue' field in the request";
    static final String ERROR_MISSING_FIELD_GPSLAT = "Missed mandatory 'gpsLat' field in the request";
    static final String ERROR_MISSING_FIELD_GPSLONG = "Missed mandatory 'gpsLong' field in the request";
    static final String ERROR_MISSING_FIELD_TIMESTAMP = "Missed mandatory 'timeStamp' field in the request";
    static final String ERROR_INVALID_FIELD_TIMESTAMP = "Filed 'timeStamp' is not a valid ISO date format";
    static final String ERROR_USERNAME_ALREADY_EXISTS = "Username already exists";
    static final String ERROR_USERNAME_NOT_EXIST = "The given username does not exist";
    static final String ERROR_DEVICE_ALREADY_PAIRED = "The wearable device is already paired with a child";
    static final String ERROR_CHILDID_NOT_EXIST = "The given childId does not exist for parent ";
    static final String ERROR_INVALID_FIELD_BIRTHDATE = "The given birthDate is not a valid date format";

    // Success messages
    static final String SUCCESS_GENERAL = "Successful operation";
    static final String SUCCESS_USER_ADDED = "User added successfully";
    static final String SUCCESS_EVENT_ADDED = "Event added successfully";
    static final String SUCCESS_CHILD_ADDED = "Child added successfully to the parent ";
}
