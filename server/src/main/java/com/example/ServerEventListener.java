package com.example;

import org.apache.mina.core.session.IoSession;

/**
 * Created by earthgee on 17/2/14.
 */
public interface ServerEventListener {

    void onUserLoginCallback(int paramInt,String paramString,IoSession paramSession);


}