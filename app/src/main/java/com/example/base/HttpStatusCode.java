package com.example.base;

/**
 * Created by DELL on 2017/4/27.
 */

public class HttpStatusCode {
    public static final int    OK                = 0;
    public static final int    UNKNOWN_ERROR     = -1;
    public static final int    LOST_PARAMS       = -2;
    public static final int    INVALID_SESSION   = -3;
    public static final int    NET_CONNECT_ERROR = -4;
    public static final int    NOT_AUTHORIZATION = -5;
    public static final int    NOT_ONLINE        = -6;
    public static final int    ERROR_SEESION     = -7;
    public static final int    TIMEOUT           = -8;
    public static final int    GATEWAY_ERROR     = -9;
    public static final int    ERROR             = -10;
    //    状态码描述
    public static final String UNKNOWN_ERROR_DES = "服务器开小差了，等会儿再试吧~";

}
