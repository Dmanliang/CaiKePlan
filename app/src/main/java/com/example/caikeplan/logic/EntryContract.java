package com.example.caikeplan.logic;

import com.example.base.BasePresenter;
import com.example.base.BaseView;
import com.example.caikeplan.logic.message.UserMessage;

import java.util.Map;

/**
 * Created by DELL on 2017/3/24.
 */

public interface EntryContract {
    interface Presenter extends BasePresenter {
        void login(Map<String, String> map);
        void register(Map<String, String> map);
        void sendCode(Map<String,String> map);
        void resetPassword(Map<String,String> map);
        void setNewPassword(Map<String,String> map);
        void addusers(Map<String,String> map);
        void validuser(Map<String,String> map);
        void managerlist(Map<String,String> map);
        void updateperson(Map<String,String> map);
        void deleteperson(Map<String,String> map);
        void message(Map<String,String> map);
        void updatePassword(Map<String,String> map);
    }

    interface View extends BaseView {
        void toHome(UserMessage userMessage);
        void toSuccessAction(String message);
        void toFailAction(String message);
    }

}
