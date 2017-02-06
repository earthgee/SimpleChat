package com.earthgee.simplechat.net.entity;

/**
 * Created by earthgee on 17/2/6.
 */
public class LoginInfo {

    private String username;
    private String password;

    public LoginInfo(String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
