package com.earthgee.simplechat.net;

import com.earthgee.simplechat.net.entity.LoginInfo;
import com.google.gson.Gson;

/**
 * Created by earthgee on 17/2/5.
 * 封装解析传输数据
 */
public class ProtocolFactory {

    public static Protocol createLoginProtocol(String username,String password){
        return new Protocol(ProtocolType.REQUEST_LOGIN,create(new LoginInfo(username,password)),-1,0);
    }

    private static String create(Object c){
        return new Gson().toJson(c);
    }

}
