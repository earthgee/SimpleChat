package com.example;

import com.example.protocal.CharsetUtil;
import com.example.protocal.Protocol;
import com.example.protocal.ProtocolFactory;
import com.example.protocal.entity.LoginInfo;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.net.SocketAddress;

/**
 * Created by earthgee on 17/2/14.
 */
public class ServerCoreHandler extends IoHandlerAdapter{

    private ServerEventListener serverEventListener=null;

    void setServerEventListener(ServerEventListener serverEventListener){
        this.serverEventListener=serverEventListener;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        //todo
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if(message instanceof IoBuffer){
            IoBuffer buffer= (IoBuffer) message;
            Protocol pFromClient=fromIOBuffer(buffer);

            String remoteAddress=clientInfoToString(session);
            switch (pFromClient.getType()){
                case REQUEST_LOGIN:
                    LoginInfo loginInfo=ProtocolFactory.parseLoginInfo(pFromClient.getContent());

                    if(this.serverEventListener!=null){
                        int tryUserId=UserProcessor.getUserIdFromSession(session);
                    }
            }
        }
    }

    public static String fromIOBuffer_JSON(IoBuffer buffer) throws Exception{
        String jsonStr=buffer.getString(CharsetUtil.decoder);
        return jsonStr;
    }

    public static Protocol fromIOBuffer(IoBuffer buffer) throws Exception{
        return ProtocolFactory.parse(fromIOBuffer_JSON(buffer),Protocol.class);
    }

    public static String clientInfoToString(IoSession session){
        SocketAddress remoteAddress=session.getRemoteAddress();
        //// TODO: 17/2/15
    }

}
