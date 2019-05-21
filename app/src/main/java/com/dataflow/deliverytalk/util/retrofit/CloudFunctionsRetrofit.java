package com.dataflow.deliverytalk.util.retrofit;

import com.dataflow.deliverytalk.util.retrofit.Services.CloudFunctionsService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CloudFunctionsRetrofit {
    private static CloudFunctionsRetrofit instance = new CloudFunctionsRetrofit();

    public static CloudFunctionsRetrofit getInstance(){
        return instance;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://asia-northeast1-deliverytalk-31595.cloudfunctions.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private CloudFunctionsService cfs = retrofit.create(CloudFunctionsService.class);
    public CloudFunctionsService getService(){
        return cfs;
    }

}
