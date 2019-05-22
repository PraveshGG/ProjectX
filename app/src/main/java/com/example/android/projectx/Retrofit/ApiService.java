package com.example.android.projectx.Retrofit;

import com.example.android.projectx.HomeScreen.PeopleFragments.PeopleModel.UserContactModel;
import com.example.android.projectx.HomeScreen.NotificationsFragments.NotificationsModel.NotificationsMain;
import com.example.android.projectx.WelcomeRegister.ModelPhnNumber;

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
    Call<List<ModelPhnNumber>> getJson();

//    @GET("5ab3910b2f00006300ca3809")//http://www.mocky.io/v2/5ab3910b2f00006300ca3809
//    Call<InfoMain> getData();
//
//    @GET("5ab3a0fe2f00006400ca385c")//http://www.mocky.io/v2/5ab3a0fe2f00006400ca385c
//    Call<NotificationMain> getNotifications();

    @GET("5ac329203000002a00f46d00") //http://www.mocky.io/v2/5ac329203000002a00f46d00
    Call<NotificationsMain> getNotifications1();

    @GET("5ac4635b3000005100f47219") // http://www.mocky.io/v2/5ac4635b3000005100f47219
    Call<NotificationsMain> getNewerNotifications();
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
