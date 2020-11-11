package com.example.riss.AppUtils;

import com.example.riss.interfaces.Api;
import com.example.riss.interfaces.ApiCallbackInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCall {


    public static void getTopFunds(final ApiCallbackInterface apiCallbackInterface) {
        final Api api = ApiUtils.getAPIService();
        Call<ResponseModel> getTopFund = api.getTopFunds();
        getTopFund.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.code() == 200) {
                    if (response.body().getResponseCode() == 1) {
                        apiCallbackInterface.onSuccess(response.body().getResponseValue());
                    } else apiCallbackInterface.onFailed(response.body().getResult());
                } else apiCallbackInterface.onFailed(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                apiCallbackInterface.onFailed(t.getLocalizedMessage());
            }
        });
    }
}
