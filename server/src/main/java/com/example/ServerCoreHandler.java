package com.example;

import org.apache.mina.core.service.IoHandlerAdapter;

/**
 * Created by earthgee on 17/2/14.
 */
public class ServerCoreHandler extends IoHandlerAdapter{

    private ServerEventListener serverEventListener=null;


    void setServerEventListener(ServerEventListener serverEventListener){
        this.serverEventListener=serverEventListener;
    }


}
