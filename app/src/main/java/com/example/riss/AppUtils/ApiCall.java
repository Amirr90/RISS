package com.example.riss.AppUtils;

import android.util.Log;

import com.example.riss.interfaces.Api;
import com.example.riss.interfaces.ApiCallbackInterface;
import com.example.riss.models.DashboardModel;
import com.example.riss.models.OTPModel;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.riss.AppUtils.Utils.getUid;

public class ApiCall {

    private static final String TAG = "ApiCall";

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
        Call<ResponseModel> getTopFund = api.distributeMedicine(
                uid,
                map.get("qty"),
                map.get("name"),
                map.get("otp"),
                map.get("mobile"),
                map.get("address"),
                System.currentTimeMillis());

        getTopFund.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.code() == 201) {
                    apiCallbackInterface.onSuccess(response.body().getResult());
                } else if (response.code() == 200) {
                    apiCallbackInterface.onFailed(response.body().getResult());
                } else apiCallbackInterface.onFailed("try again\n" + response.errorBody());

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                apiCallbackInterface.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public static void requestOtp(String number, final ApiCallbackInterface apiCallbackInterface) {
        final Api api = ApiUtils.getAPIService();

        String uid = getUid();
        Call<OTPModel> generateOtp = api.generateOtp(uid, number);

        generateOtp.enqueue(new Callback<OTPModel>() {
            @Override
            public void onResponse(@NotNull Call<OTPModel> call, @NotNull Response<OTPModel> response) {
                try {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getResponeCode() == 1) {
                            apiCallbackInterface.onSuccess(String.valueOf(response.body().getOtp()));
                        } else apiCallbackInterface.onFailed(response.body().getResult());
                    } else apiCallbackInterface.onFailed("try again");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NotNull Call<OTPModel> call, @NotNull Throwable t) {
                apiCallbackInterface.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public static void sendOtp(String otp, String number, final ApiCallbackInterface apiCallbackInterface) {
        final Api api = ApiUtils.getOtpURl();
        String authKey = "D!~5682Lrpn4QxQWG";
        String senderId = "JHASMS";
        String msg = "Use " + otp + " to verify your mobile number";

        Call<Void> generateOtp = api.sendOtp(authKey, number, senderId, msg);

        generateOtp.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.code() == 200) {
                    apiCallbackInterface.onSuccess("OTP Sent");
                    Log.d(TAG, "OTP sent Response : " + response.body());
                } else {
                    apiCallbackInterface.onFailed("try again");
                }

            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                apiCallbackInterface.onFailed(t.getLocalizedMessage());
                Log.d(TAG, "OTP  not sent Response : " + t.getLocalizedMessage());
            }
        });
    }

    public static void withdrawFund(String fundId, double amount,double amountToBank, final ApiCallbackInterface apiCallbackInterface) {

        final Api api = ApiUtils.getAPIService();
        Call<ResponseModel> call = api.withdrawFund(fundId, getUid(), amount,amountToBank);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<ResponseModel> call, @NotNull Response<ResponseModel> response) {

                if (response.code() == 200) {
                    if (null != response.body())
                        if (response.body().getResponseCode() == 1) {
                            apiCallbackInterface.onSuccess(response.body().getResult());
                        } else apiCallbackInterface.onFailed(response.message());
                } else apiCallbackInterface.onFailed("" + response.code());
            }

            @Override
            public void onFailure(@NotNull Call<ResponseModel> call, @NotNull Throwable t) {
                apiCallbackInterface.onFailed(t.getLocalizedMessage());
                Log.d(TAG, "" + t.getLocalizedMessage());
            }
        });
    }

}
