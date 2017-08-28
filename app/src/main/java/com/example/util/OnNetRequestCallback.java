package com.example.util;

/**
 * Created by DELL on 2017/3/30.
 */

public interface OnNetRequestCallback {
    void onFailed(String reason);
    void onSuccess(String response);
}
