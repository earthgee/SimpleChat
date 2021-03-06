package com.earthgee.simplechat.net;

import com.earthgee.simplechat.net.entity.ErrorInfo;
import com.earthgee.simplechat.net.entity.LoginInfo;
import com.earthgee.simplechat.net.entity.LoginResponse;
import com.earthgee.simplechat.util.CharsetUtil;
import com.google.gson.Gson;

/**
 * Created by earthgee on 17/2/5.
 * 封装解析传输数据
 */
public class ProtocolFactory {

    public static Protocol parse(byte[] fullProtocolBytes,int len){
        return parse(fullProtocolBytes, len, Protocol.class);
    }

    public static Protocol parse(byte[] fullProtocolBytes,int len,Class<Protocol> protocolClass){
        return new Gson().fromJson(CharsetUtil.getString(fullProtocolBytes, len), protocolClass);
    }

    public static <T> T parse(String ProtocolString,Class<T> protocolClass){
        return new Gson().fromJson(ProtocolString, protocolClass);
    }

    public static LoginResponse parseLoginResponse(String content){
        return parse(content, LoginResponse.class);
    }

    public static ErrorInfo parseErrorInfo(String content){
        return parse(content,ErrorInfo.class);
    }

    public static Protocol createLoginProtocol(String username,String password){
        return new Protocol(ProtocolType.REQUEST_LOGIN,create(new LoginInfo(username,password)),-1,0);
    }

    public static Protocol createChatProtocol(String content,int fromUserId,String toUserId){
        return new Protocol(ProtocolType.REQUEUST_CHAT_TEXT,content,fromUserId,
                Integer.parseInt(toUserId),true,null);
    }

    public static Protocol createKeepAlive(int fromUserId){
        return new Protocol(ProtocolType.REQUEST_KEEP_ALIVE,"",fromUserId,0);
    }

    //qos回复包
    public static Protocol createReceivedBack(int fromUserId,int toUserId,String message){
        return new Protocol(ProtocolType.RESPONSE_QOS,message/*这里是包的指纹*/,fromUserId,toUserId);
    }

    private static String create(Object c){
        return new Gson().toJson(c);
    }

}
