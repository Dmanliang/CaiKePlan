package com.example.base;

/**
 * Created by dell on 2017/5/24.
 */

public class Constants {
    public static final int     POST            = 0;                                                        //post请求方式
    public static final int     GET             = 1;                                                        //get请求方式
    public static final String  API             = "http://39.108.185.81:8001"; 					            //http://39.108.185.81:8001服务器地址
    public static final String  API2            = "http://39.108.185.81:8001"; 					            //服务器地址
    public static final String  API3            = "http://39.108.133.173:9550"; 					        //服务器地址
    public static final String  PLAYCLASS       ="/lottery/find_lottery_play_cls_list";                     //查询彩种玩法分类列表
    public static final String  PLAY_LIST       ="/lottery/find_lottery_play_list";                         //查询彩种玩法列表
    public static final String  PLAN            ="/plan/find_plan_list?lottery_id=";                        //获取玩法计划
    public static final String  PLAN_SCHEME     ="/plan/find_plan_scheme_list";                             //获取玩法计划方案
    public static final String  PLAN_FAVORITE   ="/plan/add_plan_favorite?user_id=";                        //添加计划到收藏夹
    public static final String  DELETE_PLAN     ="/plan/del_plan_favorite?user_id=";                        //从收藏夹删除计划
    public static final String  FIND_FAVORITE   ="/plan/find_user_plan_favorite?user_id=";                  //查询用户收藏夹
    public static final String  RECOMMEND       ="/plan/get_recommend?lottery_id=";                         //今日推荐
    public static final String  HOT_SCHEME      ="/plan/get_hot_scheme?lottery_id=";                        //热门计划
    public static final String  DATA_MISS       ="/plan/get_missed?lottery_id=";                            //数据分析
    public static final String  LAST_RESULT     ="/lottery/get_lottery_last_result?lottery_ids=";           //开奖数据
    public static final String  OPEN_LOTTERY    ="/lottery/get_lottery_current_issue?lottery_ids=";         //下期开奖数据
    public static final String  PLAN_RUSULT     ="/plan/find_plan_result_list?plan_id=";                    //查询计划(分页)
    public static final String  SCHEME_PLAN     ="/plan/get_schemes_by_plan?plan_id=";                      //玩法对应的计划
    public static final String  PLAN_CONTENT    ="/plan/get_plan_content?lottery_id=";                      //计划内容
    public static final String  PLAY_BY_CLS     ="/plan/get_play_by_cls?lottery_id=";                       //根据 play_cls 查询 play_id
    public static final String  PLAN_BY_PLAY    ="/plan/get_plan_by_play?lottery_id=";                      //根据 play_id 查询 plan_id
    public static final String  COPY_PLAN       ="/plan/get_plan_list_many?plan_s_ids=";                    //查询计划
    public static final String  ADV_IMAGE       ="/plan/carousel?n=";                                       //广告轮播图
    public static final String  UPDATE          ="/user/client_version_update?os=2";                        //版本更新
    public static final String  LOTTERY_HISTOEY ="/lottery/find_lottery_result_list?lottery_id=";           //彩种开奖记录
    public static final String  SERVER_TIME     ="/lottery/get_server_time";                                //服务器时间
    public static final String  VALID_USER      ="/user/valid_user";                                        //查看用户名或者手机是否存在
    public static final String  USER_LOGIN      ="/user/user_login";                                        //用户登入
    public static final String  BIND_PHONE      ="/user/bind_phone";                                        //绑定手机
    public static final String  EDIT_PASWD      ="/user/edit_password";                                     //修改密码
    public static final String  SEND_PHONE      ="/user/send_phone_code";                                   //发送验证码到手机
    public static final String  RESET_PASWD     ="/user/reset_password";                                    //邮箱重置密码
    public static final String  ADD_USER        ="/user/add_user";                                          //客户端添加用户
    public static final String  USER_LIST       ="/user/manager_user_list";                                 //用户管理列表
    public static final String  UPDATE_POWER    ="/user/update_power";                                      //修改下线权限
    public static final String  SET_OFFINE_PSD  ="/user/update_offine_password";                            //修改下线密码
    public static final String  DELETE_USER     ="/user/del_user";                                          //删除下线
    public static final String  NOTICE_LIST     ="/user/get_notice_list";                                   //获取公告列表
    public static final String  CALCULATOR      ="/static/jisuanqi.html";                                   //计算器
    public static final String  SSC_VIDEO       ="/static/ssc/index.html?lottery_id=";                      //时时彩开奖视频
    public static final String  PK10_VIDEO      ="/static/pk10/index.html?lottery_id=";                     //pk10开奖视频
    public static final String  GD11X5_VIDEO    ="/static/11x5/index.html?lottery_id=";                     //11选5开奖视频
    public static final String  K3_VIDEO        ="/static/k3/index.html?lottery_id=";                       //快3开奖视频
    public static final String  ALL_TOOLS       ="/static/tool/index.html";                                 //总工具列表

}
