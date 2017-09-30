package com.example.util;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpManager implements Callback {

    private OkHttpClient         mClient;
    private Map<String, String>  sMap;
    private String               token;
    private OnNetRequestCallback mCallback = null;
    private MHandler             mMHandler;

    public OKHttpManager() {
        mClient = new OkHttpClient();
        sMap = new HashMap<>();
        mMHandler = new MHandler(this);
    }

    public Headers headers() {
        sMap.put("token",token);
        Headers.Builder builder = new Headers.Builder();
        if (sMap != null) {
            for (Map.Entry<String, String> entry : sMap.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            if (token != null) {
                builder.add("Authorization", token);
            }
        }
        return builder.build();
    }

    public void setToken(String token) {
        this.token = token;
    }

    private RequestBody addRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> map :
                params.entrySet()) {
            builder.add(map.getKey(), map.getValue());
        }
        return builder.build();
    }

    /**
     * Post 请求
     *
     * @param url        api地址
     * @param bodyParams body参数
     * @param callback   请求回调接口
     * @param header     是否包含请求头
     */
    public void post(String url, Map<String, String> bodyParams, OnNetRequestCallback callback, boolean header) {
        this.mCallback = callback;
        Request.Builder builder = new Request.Builder();
        if (header) {
            builder.headers(headers());
        }
        builder.url(url).post(addRequestBody(bodyParams));
        mClient.newCall(builder.build()).enqueue(this);
    }

    /**
     * Get请求
     *
     * @param url      api地址
     * @param params   路径参数
     * @param callback 请求回调接口
     */
    public void get(String url, Map<String, String> params, OnNetRequestCallback callback) {
        this.mCallback = callback;
        Request request = new Request.Builder()
                .url(attachParams(url, params))//拼接Url
                .get()
                .build();
        mClient.newCall(request).enqueue(this);
    }

    public String attachParams(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        builder.append("?");
        for (Map.Entry<String, String> e :
                params.entrySet()) {
            builder.append(e.getKey()).append("=").append(e.getValue());
        }
        return builder.toString();
    }

    private static final int FAILED  = 0;
    private static final int SUCCESS = 1;
    private static final int NONE    = -1;


    private static class MHandler extends Handler {
        WeakReference<OKHttpManager> mReference = null;

        public MHandler(OKHttpManager okHttpManager) {
            mReference = new WeakReference<>(okHttpManager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                if (msg.what == FAILED) {
                    if(mReference != null) {
                        mReference.get().mCallback.onFailed((String) msg.obj);
                    }
                } else if (msg.what == SUCCESS) {
                    if(mReference != null){
                        mReference.get().mCallback.onSuccess((String) msg.obj);
                    }
                } else {
                    if(mReference !=null) {
                        mReference.get().mCallback.onSuccess((String) msg.obj);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //网络请求之后的回调
    @Override
    public void onFailure(okhttp3.Call call, IOException e) {
        Message m = mMHandler.obtainMessage();
        m.obj = "服务器或网络出现了问题，请稍后重试";
        m.what = FAILED;
        mMHandler.sendMessage(m);
    }

    @Override
    public void onResponse(okhttp3.Call call, Response response) throws IOException {
        String  res     = response.body().string();
        Message message = mMHandler.obtainMessage();
        try {
            JSONObject jsonObject = new JSONObject(res);
            if (jsonObject.getString("success").equals("1")) {
                message.what = SUCCESS;
            } else {
                message.what = FAILED;
            }
            message.obj = res;
        } catch (Exception e) {
            message.what = NONE;
            message.obj = "服务器或网络出现了问题，请稍后重试";
        }finally {
            mMHandler.sendMessage(message);
        }
    }
}
