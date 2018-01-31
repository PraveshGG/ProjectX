package com.example.android.projectx;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Pravesh on 1/16/2018.
 */

public interface ApiService {
    @GET("5a5d91b6330000060419180c") //http://www.mocky.io/v2/5a5d91b6330000060419180c
    Call<List<ModelClass>> getJson();
}
