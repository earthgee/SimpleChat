package com.example.protocal;

/**
 * Created by earthgee on 17/2/5.
 */
public enum ProtocolType {

    //发送
    REQUEST_LOGIN(0),
    //接收
    RESPONSE_LOGIN(1),

    //发送文字信息
    REQUEUST_CHAT_TEXT(2),

    //发送心跳包信息
    REQUEST_KEEP_ALIVE(3),

    //回复心跳包消息
    RESPONSE_KEEP_ALIVE(4),

    //客户端回复qos
    RESPONSE_QOS(5),

    //错误
    RESPONSE_ERROR(100)

    ;

    private int index;

    ProtocolType(int index){
        this.index=index;
    }

    int getIndex(){
        return index;
    }

}

