package com.example.riss.interfaces;

public interface ApiCallbackInterface {
    void onSuccess(Object obj);

    void onFailed(String msg);
}
