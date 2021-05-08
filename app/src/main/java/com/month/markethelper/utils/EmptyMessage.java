package com.month.markethelper.utils;

public class EmptyMessage {

    public static final int STATE_LOGIN = 0;    //登录
    public static final int STATE_LOGOUT = 1;   //注销
    public static final int STATE_STORE = 2;    //商家

    public final int code;

    public static EmptyMessage getInstance(int code) {
        return new EmptyMessage(code);
    }

    private EmptyMessage(int code) {
        this.code = code;
    }
}
