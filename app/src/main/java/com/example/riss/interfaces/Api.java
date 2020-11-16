package com.example.riss.interfaces;

import android.content.Intent;

import com.example.riss.AppUtils.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @GET("getTopFunds")
    Call<ResponseModel> getTopFunds();


    @FormUrlEncoded
    @POST("requsestMedicine")
    Call<Void> requestMedicine(
            @Field("uid") String uid,
            @Field("mId") String mId,
            @Field("qty") String qty,
            @Field("timestamp") Long timestamp
    );

    @FormUrlEncoded
    @POST("distributeMedicine")
    Call<Void> distributeMedicine(
            @Field("uid") String uid,
            @Field("qty") String qty,
            @Field("name") String name,
            @Field("otp") String otp,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("timestamp") Long timestamp
    );
}
