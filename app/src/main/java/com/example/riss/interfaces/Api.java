package com.example.riss.interfaces;

import com.example.riss.AppUtils.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("getTopFunds")
    Call<ResponseModel>getTopFunds();
}
