package com.month.markethelper.utils;

public class EmptyMessage {

    public static final int STATE_USER_LOGIN = 0;    //用户登录
    public static final int STATE_USER_LOGOUT = 1;   //用户注销
    public static final int STATE_STORE_LOGIN = 2;   //商家登录
    public static final int STATE_STORE_LOGOUT = 3;  //商家注销

    public final int code;

    public static EmptyMessage getInstance(int code) {
        return new EmptyMessage(code);
    }

    private EmptyMessage(int code) {
        this.code = code;
    }
}
