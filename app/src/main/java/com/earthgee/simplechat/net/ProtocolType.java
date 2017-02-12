package com.earthgee.simplechat.net;

/**
 * Created by earthgee on 17/2/5.
 */
public enum ProtocolType {

    //发送
    REQUEST_LOGIN(0),
    //接收
    RESPONSE_LOGIN(1);

    private int index;

    ProtocolType(int index){
        this.index=index;
    }

    int getIndex(){
         return index;
    }

}
