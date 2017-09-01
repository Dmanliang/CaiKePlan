package com.example.caikeplan.logic;

import android.content.Context;

import com.example.base.Constants;
import com.example.base.HttpStatusCode;
import com.example.caikeplan.logic.message.NoticeMessage;
import com.example.caikeplan.logic.message.PersonMessage;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.util.OKHttpManager;
import com.example.util.OnNetRequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EntryPresenter implements EntryContract.Presenter {
    private Context     context;
    private EntryContract.View  mView;
    private Map<String, String> map;
    private OKHttpManager instance;

    private static final int FAILED  = 0;
    private static final int SUCCESS = 1;

    public EntryPresenter(EntryContract.View view,Context context) {
        this.mView = view;
        this.map = new HashMap<>();
        instance = new OKHttpManager();
        this.context = context;
    }

    @Override
    public void login(Map<String, String> map) {
        this.map = map;
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.USER_LOGIN, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    try {
                        JSONObject jsonObject = new JSONObject(reason);
                        if (jsonObject.getInt("success") == HttpStatusCode.ERROR_SYSTEM) {
                            mView.onMessage(HttpStatusCode.UNKNOWN_ERROR_DES);
                        }else if(jsonObject.getInt("success") == HttpStatusCode.USER_PAS){
                            mView.onMessage(HttpStatusCode.USER_PAS_DES);
                        } else if(jsonObject.getInt("success") == HttpStatusCode.FREEZE){
                            mView.onMessage(HttpStatusCode.FREEZE_DES);
                        }else if(jsonObject.getInt("success") == HttpStatusCode.OVERTIME){
                            mView.onMessage(HttpStatusCode.OVERTIME_DES);
                        }else{
                            mView.onMessage(jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject o         = new JSONObject(response);
                    String     data      = o.getString("data");
                    String     username  = o.getJSONObject("data").getString("username");
                    String     user_id   = o.getJSONObject("data").getString("user_id");
                    String     token     = o.getJSONObject("data").getString("token");
                    String     role_id   = o.getJSONObject("data").getString("role_id");
                    String     due_time  = o.getJSONObject("data").getString("due_time");
                    String     phone     = o.getJSONObject("data").getString("phone");
                    String     power_add = o.getJSONObject("data").getString("power_add");
                    String     parent_id = o.getJSONObject("data").getString("parent_id");
                    String     use_count = o.getJSONObject("data").getString("use_count");
                    String     all_count = o.getJSONObject("data").getString("all_count");
                    UserMessage.getInstance().setUsername(username);
                    UserMessage.getInstance().setUser_id(user_id);
                    UserMessage.getInstance().setToken(token);
                    UserMessage.getInstance().setRole_id(role_id);
                    UserMessage.getInstance().setDue_time(due_time);
                    UserMessage.getInstance().setPhone(phone);
                    UserMessage.getInstance().setPower_add(power_add);
                    UserMessage.getInstance().setParent_id(parent_id);
                    UserMessage.getInstance().setUse_count(use_count);
                    UserMessage.getInstance().setAll_count(all_count);
                    if (mView != null) {
                        mView.disableLoadingAnimation();
                        mView.toHome(UserMessage.getInstance());
                    }
                } catch (JSONException e) {
                    mView.disableLoadingAnimation();
                    e.printStackTrace();
                }
            }
        }, false);
    }

    @Override
    public void register(Map<String, String> map) {
        this.map = map;
        mView.showLoadingAnimation();
        instance.setToken(map.get("token"));
        instance.post(Constants.API2+Constants.BIND_PHONE, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.REGISTE_DES);
                    } else {
                        mView.onMessage(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    ToastUtil.getShortToastByString(context,"手机绑定成功");
                    mView.toHome(UserMessage.getInstance());
                }
            }
        }, true);
    }

    @Override
    public void sendCode(Map<String, String> map) {
        this.map = map;
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.SEND_PHONE, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.SENDCODE_DES);
                    } else {
                        mView.onMessage(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        mView.onMessage(jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mView.toSuccessAction("code");
                    ToastUtil.getShortToastByString(context,"验证码发送成功");
                }
            }
        }, false);
    }

    @Override
    public void resetPassword(Map<String, String> map) {
        this.map = map;
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.RESET_PASWD, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.RESET_DES);
                    } else {
                        mView.onMessage(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    ToastUtil.getShortToastByString(context,"密码重设成功");
                    mView.toSuccessAction("psw");
                }
            }
        }, false);
    }

    @Override
    public void setNewPassword(Map<String, String> map) {
        this.map = map;
        instance.setToken(map.get("token"));
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.EDIT_PASWD, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.PASSWORD_DES);
                    } else {
                        mView.onMessage(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    ToastUtil.getShortToastByString(context,"密码修改成功");
                    mView.toSuccessAction("reset");
                }
            }
        }, true);
    }

    @Override
    public void addusers(Map<String, String> map) {
        this.map = map;
        instance.setToken(map.get("token"));
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.ADD_USER, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.ADDUSER_DES);
                    } else {
                        mView.onMessage(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    ToastUtil.getShortToastByString(context,"开户成功");
                    mView.toHome(UserMessage.getInstance());
                    int use_count = Integer.parseInt(UserMessage.getInstance().getUse_count())+1;
                    UserMessage.getInstance().setUse_count(use_count+"");
                }
            }
        }, true);
    }

    @Override
    public void validuser(final Map<String, String> map) {
        this.map = map;
        instance.setToken(map.get("token"));
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.VALID_USER, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                    } else {
                        mView.toFailAction(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    try {
                        JSONObject o = new JSONObject(response);
                        String data = o.getString("data");
                        if(map.get("type").equals("0")){
                            if(data.equals("0")){
                                mView.toFailAction("用户名已存在");
                            }else{
                                mView.toSuccessAction("用户名通过");
                            }
                        }else if(map.get("type").equals("1")){
                            if(data.equals("0")){
                                mView.toFailAction("手机已绑定");
                            }else{
                                mView.toSuccessAction("手机通过");
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, true);
    }

    @Override
    public void managerlist(Map<String, String> map) {
        this.map = map;
        instance.setToken(map.get("token"));
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.USER_LIST, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.MANLIST_DES);
                    } else {
                        mView.toFailAction(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    try {
                        JSONObject o = new JSONObject(response);
                        JSONArray data = o.getJSONArray("data");
                        PersonMessage personMessage;
                        List<PersonMessage> list = new ArrayList<>();
                        for(int i=0;i<data.length();i++){
                            String user_id  = data.getJSONObject(i).getString("user_id");
                            String username = data.getJSONObject(i).getString("username");
                            String due_time = data.getJSONObject(i).getString("due_time");
                            String power_add= data.getJSONObject(i).getString("power_add");
                            personMessage = new PersonMessage(user_id,username,due_time,power_add);
                            list.add(personMessage);
                        }
                        UserMessage.getInstance().getPersonlist().clear();
                        UserMessage.getInstance().getPersonlist().addAll(list);
                        mView.toSuccessAction("");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, true);
    }

    @Override
    public void updateperson(Map<String, String> map) {
        this.map = map;
        instance.setToken(map.get("token"));
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.UPDATE_POWER, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.UPDATE_DES);
                    } else {
                        mView.toFailAction(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    mView.onMessage("用户修改成功");
                    mView.toSuccessAction("");
                }
            }
        }, true);
    }

    @Override
    public void deleteperson(Map<String, String> map) {
        this.map = map;
        instance.setToken(map.get("token"));
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.DELETE_USER, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.DELETE_DES);
                    } else {
                        mView.toFailAction(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    mView.onMessage("用户删除成功");
                    mView.toSuccessAction("");
                    int use_count = Integer.parseInt(UserMessage.getInstance().getUse_count())-1;
                    UserMessage.getInstance().setUse_count(use_count+"");
                }
            }
        }, true);
    }

    @Override
    public void message(Map<String, String> map) {
        this.map = map;
        instance.setToken(map.get("token"));
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.NOTICE_LIST, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.MESSAGE_DES);
                    } else {
                        mView.toFailAction(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray  data = jsonObject.getJSONArray("data");
                        NoticeMessage noticeMessage;
                        if(data.length() != UserMessage.getInstance().getNoticeMessageList().size()){
                            ArrayList<NoticeMessage> noticeMessages = new ArrayList<>();
                            for(int i=0;i<data.length();i++){
                                jsonObject = data.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String url  = jsonObject.getString("url");
                                String notice_id = jsonObject.getString("notice_id");
                                String send_id  =   jsonObject.getString("send_id");
                                String send_name = jsonObject.getString("send_name");
                                String create_at = jsonObject.getString("created_at");
                                String update_at = jsonObject.getString("update_at");
                                noticeMessage = new NoticeMessage(notice_id,send_id,send_name,name,url,create_at,update_at);
                                noticeMessages.add(noticeMessage);
                            }
                            UserMessage.getInstance().getNoticeMessageList().clear();
                            UserMessage.getInstance().getNoticeMessageList().addAll(noticeMessages);
                            mView.toSuccessAction("");
                        }else{
                            mView.onMessage("");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }, true);
    }

    @Override
    public void updatePassword(Map<String, String> map) {
        this.map = map;
        instance.setToken(map.get("token"));
        mView.showLoadingAnimation();
        instance.post(Constants.API2+Constants.SET_OFFINE_PSD, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                mView.disableLoadingAnimation();
                try {
                    JSONObject jsonObject = new JSONObject(reason);
                    if (jsonObject.getInt("success") == HttpStatusCode.UNKNOWN_ERROR) {
                        mView.onMessage(HttpStatusCode.PASSWORD_DES);
                    } else {
                        mView.onMessage(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {
                if (mView != null) {
                    mView.disableLoadingAnimation();
                    ToastUtil.getShortToastByString(context,"密码修改成功");
                    mView.toSuccessAction("reset");
                }
            }
        }, true);
    }


}
