package com.example.riss.AppUtils;

import com.example.riss.models.Fund;

import java.util.List;

public class ResponseModel {
    int responseCode;
    String result;
    List<Fund> responseValue;

    public int getResponseCode() {
        return responseCode;
    }

    public String getResult() {
        return result;
    }

    public List<?> getResponseValue() {
        return responseValue;
    }
}
