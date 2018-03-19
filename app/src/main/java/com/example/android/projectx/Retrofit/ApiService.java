package com.example.android.projectx.Retrofit;

import com.example.android.projectx.CustomModel.UserContactModel;
import com.example.android.projectx.ModelClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Pravesh on 1/16/2018.
 */

public interface ApiService {
    @GET("5a5d91b6330000060419180c") //http://www.mocky.io/v2/5a5d91b6330000060419180c
    Call<List<ModelClass>> getJson();

    @Headers("content-type: application/json")
    @POST("api/v1/synccontact/insert")
    Call<Object> sendContact(@Body List<UserContactModel> userContactModel);

    @Headers("content-type: application/json")
    @POST("api/v1/synccontact/update")
    Call<Object> editContact(@Body List<UserContactModel> userEditContactModel);

    @Headers("content-type: application/json")
    @POST("api/v1/synccontact/delete")
    Call<Object> deleteContact(@Body List<UserContactModel> userDeleteContactModel);
}
