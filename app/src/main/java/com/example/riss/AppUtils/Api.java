package com.example.riss.AppUtils;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("getTopFunds")
    Call<ResponseModel>getTopFunds();
}
