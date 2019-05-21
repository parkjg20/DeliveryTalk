package com.dataflow.deliverytalk.util.retrofit.Services;

import com.dataflow.deliverytalk.Models.User;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CloudFunctionsService {

    @POST("/deleteUser")
    Call<Void> deleteUser(@Body User user);
}
 