package com.example.android.projectx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pravesh on 1/16/2018.
 */

public class ApiClient {

    private static final String ROOT_URL= "http://www.mocky.io/v2/";
    private static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }
    public static ApiService getApiService()
    {
        return getRetrofitInstance().create(ApiService.class);
    }
}
