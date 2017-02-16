package com.example;

import org.apache.mina.core.session.IoSession;

/**
 * Created by earthgee on 17/2/14.
 */
public class ServerEventListenerImpl implements ServerEventListener{

    @Override
    public void onUserLoginCallback(int userId, String userName, IoSession paramSession) {
        System.out.println("userId="+userId+",userName="+userName+" 登录");
    }
}
