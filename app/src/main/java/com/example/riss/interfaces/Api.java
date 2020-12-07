package com.example.riss.interfaces;

import android.content.Intent;

import com.example.riss.AppUtils.ResponseModel;
import com.example.riss.models.DashboardModel;
import com.example.riss.models.OTPModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("getTopFunds")
    Call<ResponseModel> getTopFunds();

    @GET("getUserDashboard")
    Call<DashboardModel> getDashboardData(@Query("uid") String uid);


    @FormUrlEncoded
    @POST("requsestMedicine")
    Call<Void> requestMedicine(
            @Field("uid") String uid,
            @Field("mId") String mId,
            @Field("qty") String qty,
            @Field("timestamp") Long timestamp
    );


    @GET("generateOtp")
    Call<OTPModel> generateOtp(
            @Query("uid") String uid,
            @Query("mobileNumber") String mobileNumber
    );

    @GET("sms")
    Call<Void> sendOtp(
            @Query("auth") String auth,
            @Query("msisdn") String msisdn,
            @Query("senderid") String senderid,
            @Query("message") String mobileNumber
    );


    @FormUrlEncoded
    @POST("distributeMedicine")
    Call<ResponseModel> distributeMedicine(
            @Field("uid") String uid,
            @Field("qty") String qty,
            @Field("name") String name,
            @Field("otp") String otp,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("timestamp") Long timestamp,
            @Field("fundID") String fundID
    );


    @POST("withdrawFundAmount")
    Call<ResponseModel> withdrawFund(
            @Query("fundId") String fundId,
            @Query("uid") String uid,
            @Query("amount") double amount,
            @Query("amountToWithdraw") double amountToBank
    );
}
