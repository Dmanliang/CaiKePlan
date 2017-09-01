package com.example.base;

/**
 * Created by DELL on 2017/4/27.
 */

public class HttpStatusCode {
    public static final int     ERROR_SYSTEM      = 0;
    public static final int     UNKNOWN_ERROR     = -1;
    public static final int     LOST_PARAMS       = -2;
    public static final int     INVALID_SESSION   = -3;
    public static final int     NET_CONNECT_ERROR = -4;
    public static final int     NOT_AUTHORIZATION = -5;
    public static final int     NOT_ONLINE        = -6;
    public static final int     ERROR_SEESION     = -7;
    public static final int     TIMEOUT           = -8;
    public static final int     GATEWAY_ERROR     = -9;
    public static final int     ERROR             = -10;
    public static final int     USER_PAS          = -10001;
    public static final int     FREEZE            = -10002;
    public static final int     OVERTIME          = -10003;
    //    状态码描述
    public static final String UNKNOWN_ERROR_DES    = "登录失败";
    public static final String USER_PAS_DES         = "用户名密码不匹配";
    public static final String FREEZE_DES           = "账号已冻结";
    public static final String OVERTIME_DES         = "账号已过期";
    public static final String REGISTE_DES          = "手机绑定失败";
    public static final String SENDCODE_DES         = "验证码发送失败";
    public static final String RESET_DES            = "密码重置失败";
    public static final String PASSWORD_DES         = "密码修改失败";
    public static final String ADDUSER_DES          = "添加用户失败";
    public static final String MANLIST_DES          = "下线用户数据加载失败";
    public static final String UPDATE_DES           = "修改用户失败";
    public static final String DELETE_DES           = "删除用户失败";
    public static final String MESSAGE_DES          = "加载公告消息失败";

}
