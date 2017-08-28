package com.example.caikeplan.logic.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/15.
 */

public class UserMessage {

    private String              username;
    private String              user_id;
    private String              token;
    private String              role_id;
    private String              due_time;
    private String              email;
    private String              power_add;
    private String              parent_id;
    private List<PersonMessage> personlist = new ArrayList<>();
    private List<NoticeMessage> noticeMessageList = new ArrayList<>();
    public UserMessage(){

    }

    public UserMessage(String username,String user_id,String token,String role_id,String due_time,String email,String power_add,String parent_id){
        this.username = username;
        this.user_id  = user_id;
        this.token    = token;
        this.role_id  = role_id;
        this.due_time = due_time;
        this.email    = email;
        this.power_add= power_add;
        this.parent_id= parent_id;
    }

    public List<NoticeMessage> getNoticeMessageList() {
        return noticeMessageList;
    }

    public void setNoticeMessageList(List<NoticeMessage> noticeMessageList) {
        this.noticeMessageList = noticeMessageList;
    }

    public List<PersonMessage> getPersonlist() {
        return personlist;
    }

    public void setPersonlist(List<PersonMessage> personlist) {
        this.personlist = personlist;
    }

    public static void setInstance(UserMessage instance) {
        UserMessage.instance = instance;
    }

    public static UserMessage instance = new UserMessage();

    public static UserMessage getInstance(){
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPower_add() {
        return power_add;
    }

    public void setPower_add(String power_add) {
        this.power_add = power_add;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
