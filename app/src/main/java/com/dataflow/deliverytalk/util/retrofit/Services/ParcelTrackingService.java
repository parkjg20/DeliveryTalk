package com.dataflow.deliverytalk.util.retrofit.Services;

import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.Models.User;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ParcelTrackingService {

    @GET("/carriers")
    Call<List<Carrier>> getCarriers();


    @GET("/carriers/{carrierId}/tracks/{waybill}")
    Call<JsonObject> parcelTrack(@Path("carrierId") String carrierId, @Path("waybill") String waybill);
}
