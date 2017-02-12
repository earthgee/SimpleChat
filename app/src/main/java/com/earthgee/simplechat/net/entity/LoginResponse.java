package com.earthgee.simplechat.net.entity;

/**
 * Created by earthgee on 17/2/12.
 */
public class LoginResponse {

    private int code;
    private int userId;

    public LoginResponse(int code, int userId) {
        this.code = code;
        this.userId = userId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
