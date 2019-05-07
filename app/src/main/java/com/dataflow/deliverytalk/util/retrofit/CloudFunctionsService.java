package com.dataflow.deliverytalk.util.retrofit;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CloudFunctionsService {

    @POST("deleteUser")
    Call<Void> deleteUser(@Body String uid);
}
