package com.example.protocal;

import com.example.protocal.entity.LoginInfo;
import com.example.protocal.entity.LoginResponse;
import com.google.gson.Gson;

/**
 * Created by earthgee on 17/2/5.
 * 封装解析传输数据
 */
public class ProtocolFactory {

    public static Protocol parse(byte[] fullProtocolBytes,int len){
        return parse(fullProtocolBytes,len,Protocol.class);
    }

    public static Protocol parse(byte[] fullProtocolBytes,int len,Class<Protocol> protocolClass){
        return new Gson().fromJson(CharsetUtil.getString(fullProtocolBytes, len), protocolClass);
    }

    public static <T> T parse(String ProtocolString,Class<T> protocolClass){
        return new Gson().fromJson(ProtocolString,protocolClass);
    }

    public static LoginResponse parseLoginResponse(String content){
        return parse(content, LoginResponse.class);
    }

    public static LoginInfo parseLoginInfo(String content){
        return parse(content,LoginInfo.class);
    }

    public static Protocol createLoginProtocol(String username,String password){
        return new Protocol(ProtocolType.REQUEST_LOGIN,create(new LoginInfo(username,password)),-1,0);
    }

    private static String create(Object c){
        return new Gson().toJson(c);
    }

}
