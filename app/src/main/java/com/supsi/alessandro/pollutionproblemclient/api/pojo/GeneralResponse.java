package com.supsi.alessandro.pollutionproblemclient.api.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alessandro on 01/05/2017.
 *
 * This Generic class represents the general response received from our API, that consists in a json response
 * with 3 different optional field:
 *
 * - body : if no error occurred, it contains the requested resource or the posted resource.
 *          Body is generalized with T because its content depends on the request that will be done.
 *          Check others subclasses in the pojo package to see the possible responses.
 * - error : is set if any error occurs with a description of it. If this field is set no body or message field is set.
 * - message : if no error occurred, it contains a successful message with a description of what has been done.
 *
 *
 */
public class GeneralResponse<T> {
    @SerializedName("body")
    private T body;

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    public T getBody() {
        return body;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "body=" + body +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
