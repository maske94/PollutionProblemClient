package com.supsi.alessandro.pollutionproblemclient.api.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alessandro on 01/05/2017.
 *
 * This Generic class represents the general response given from the API, that consists in a json response
 * with 3 different optional field:
 *
 * - error : is set if any error occurs with a description of it. If set no body or message filed is set.
 * - body : if no error occurred, it contains the requested resource or the posted resource.
 *          Body is generic T because it depends on the request that will be done.
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

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "body=" + body +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
