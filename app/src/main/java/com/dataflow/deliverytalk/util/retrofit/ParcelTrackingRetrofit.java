package com.dataflow.deliverytalk.util.retrofit;

import com.dataflow.deliverytalk.util.retrofit.Services.ParcelTrackingService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParcelTrackingRetrofit {
    private static ParcelTrackingRetrofit instance = new ParcelTrackingRetrofit();

    public static ParcelTrackingRetrofit getInstance(){
        return instance;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://apis.tracker.delivery")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private ParcelTrackingService cfs = retrofit.create(ParcelTrackingService.class);
    public ParcelTrackingService getService(){
        return cfs;
    }
}
