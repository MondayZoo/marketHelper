package com.month.markethelper.utils;

public class EmptyMessage {

    public static final int STATE_LOGIN = 0;
    public static final int STATE_LOGOUT = 1;

    public final int code;

    public static EmptyMessage getInstance(int code) {
        return new EmptyMessage(code);
    }

    private EmptyMessage(int code) {
        this.code = code;
    }
}
