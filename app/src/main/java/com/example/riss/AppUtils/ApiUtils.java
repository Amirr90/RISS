package com.example.riss.AppUtils;

import com.example.riss.interfaces.Api;

public class ApiUtils {

    private ApiUtils() {
    }

    public static final String BASE_URL = "https://us-central1-riss-68d96.cloudfunctions.net/";
    public static final String OTP_URL = "https://api.datagenit.com/";

    public static Api getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(Api.class);

    }

    public static Api getOtpURl() {

        return RetrofitClient.getClient(OTP_URL).create(Api.class);

    }
}