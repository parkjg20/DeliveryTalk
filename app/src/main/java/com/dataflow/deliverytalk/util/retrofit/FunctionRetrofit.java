package com.dataflow.deliverytalk.util.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FunctionRetrofit {
    private static FunctionRetrofit instance = new FunctionRetrofit();

    public static FunctionRetrofit getInstance(){
        return instance;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://us-central1-deliverytalk-31595.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private CloudFunctionsService cfs = retrofit.create(CloudFunctionsService.class);
    public CloudFunctionsService getService(){
        return cfs;
    }

}
