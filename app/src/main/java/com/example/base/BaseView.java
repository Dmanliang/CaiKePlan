package com.example.base;

/**
 * Created by DELL on 2017/3/24.
 */

public interface BaseView {
    void showLoadingAnimation();

    void disableLoadingAnimation();

    void onMessage(String message);
}
