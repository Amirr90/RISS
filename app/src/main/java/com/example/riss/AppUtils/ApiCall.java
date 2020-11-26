package com.example.riss.AppUtils;

import com.example.riss.interfaces.Api;
import com.example.riss.interfaces.ApiCallbackInterface;
import com.example.riss.models.DashboardModel;

import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.riss.AppUtils.Utils.getUid;

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

    public static void getDashboardData(final ApiCallbackInterface apiCallbackInterface) {
        final Api api = ApiUtils.getAPIService();
        Call<DashboardModel> getTopFund = api.getDashboardData(getUid());
        getTopFund.enqueue(new Callback<DashboardModel>() {
            @Override
            public void onResponse(Call<DashboardModel> call, Response<DashboardModel> response) {
                if (response.code() == 200) {
                    if (response.body().getResponseCode() == 1) {
                        apiCallbackInterface.onSuccess(response.body());
                    } else apiCallbackInterface.onFailed(response.body().getResult());
                } else apiCallbackInterface.onFailed(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<DashboardModel> call, Throwable t) {
                apiCallbackInterface.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public static void requestMedicine(String qty, final ApiCallbackInterface apiCallbackInterface) {
        final Api api = ApiUtils.getAPIService();

        String uid = getUid();
        String mid = UUID.randomUUID().toString();
        Call<Void> getTopFund = api.requestMedicine(uid, mid, qty, System.currentTimeMillis());

        getTopFund.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    apiCallbackInterface.onSuccess(response.body());
                } else apiCallbackInterface.onFailed("try again");

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                apiCallbackInterface.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public static void distributeMedicine(Map<String, String> map, final ApiCallbackInterface apiCallbackInterface) {
        final Api api = ApiUtils.getAPIService();

        String uid = getUid();
        String mid = UUID.randomUUID().toString();
        Call<Void> getTopFund = api.distributeMedicine(
                uid,
                map.get("qty"),
                map.get("name"),
                map.get("otp"),
                map.get("mobile"),
                map.get("address"),
                System.currentTimeMillis());

        getTopFund.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    apiCallbackInterface.onSuccess(response.body());
                } else apiCallbackInterface.onFailed("try again\n" + response.errorBody());

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                apiCallbackInterface.onFailed(t.getLocalizedMessage());
            }
        });
    }
}
