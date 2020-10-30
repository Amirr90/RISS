package com.example.riss.AppUtils;

import com.example.riss.FundsModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("getTopFunds")
    Call<FundsModel> getFunds();
}
