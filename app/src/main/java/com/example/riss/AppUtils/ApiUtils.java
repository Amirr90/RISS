package com.example.riss.AppUtils;

import com.example.riss.interfaces.Api;

public class ApiUtils {

    private ApiUtils() {
    }

    public static final String BASE_URL = "https://us-central1-riss-68d96.cloudfunctions.net/";

    public static Api getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(Api.class);

    }
}