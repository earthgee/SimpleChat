package com.example;

import org.apache.mina.core.session.IoSession;

import java.util.Date;

/**
 * Created by earthgee on 17/2/14.
 */
public class ServerEventListenerImpl implements ServerEventListener{

    @Override
    public void onUserLoginCallback(int userId, String userName, IoSession paramSession) {
        System.out.println("userId="+userId+",userName="+userName+" 登录");
    }

    @Override
    public void onReceiveKeepAlive(int userId) {
        System.out.println("收到" + userId + "发来的心跳包,时间是" + new Date(System.currentTimeMillis()));
    }


}
