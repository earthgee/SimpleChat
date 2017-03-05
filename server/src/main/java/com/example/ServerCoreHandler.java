package com.example;

import com.example.protocal.CharsetUtil;
import com.example.protocal.Protocol;
import com.example.protocal.ProtocolFactory;
import com.example.protocal.UserProcessor;
import com.example.protocal.entity.LoginInfo;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
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
                        int tryUserId= UserProcessor.getUserIdFromSession(session);

                        boolean alreadyLogined=tryUserId!=-1;

                        if(alreadyLogined){
                            //已经登录过 这里重新登录一次
                            boolean sendOk=sendData(session,
                                    ProtocolFactory.createLoginResponse(0,tryUserId));
                            if(sendOk){
                                session.setAttribute
                                        (UserProcessor.USER_ID_IN_SESSION_ATTRIBUTE,tryUserId);
                                session.setAttribute
                                        (UserProcessor.LOGIN_NAME_IN_SESSION_ATTRIBUTE,loginInfo.getUsername());
                                UserProcessor.getInstance().putUser(tryUserId,session,loginInfo.getUsername());
                                serverEventListener.onUserLoginCallback(tryUserId,loginInfo.getUsername(),session);
                            }
                        }else{
                            int userId=getNextUserId(loginInfo);

                            boolean sendOk=sendData(session,
                                    ProtocolFactory.createLoginResponse(0, userId));
                            if(sendOk){
                                session.setAttribute
                                        (UserProcessor.USER_ID_IN_SESSION_ATTRIBUTE,userId);
                                session.setAttribute
                                        (UserProcessor.LOGIN_NAME_IN_SESSION_ATTRIBUTE,loginInfo.getUsername());
                                UserProcessor.getInstance().putUser(userId,session,loginInfo.getUsername());
                                serverEventListener.onUserLoginCallback(tryUserId,loginInfo.getUsername(),session);
                            }
                        }
                        break;
                    }
                    break;
                case REQUEST_KEEP_ALIVE:
                    if(serverEventListener!=null){
                        serverEventListener.onReceiveKeepAlive(pFromClient.getFrom());
                    }
                    //可能某些原因没有及时发心跳，这时服务端已认为掉线，发送重新登录的消息
                    if(!UserProcessor.isLogined(session)){
                        sendData(session,ProtocolFactory.createErrorResponse(301,pFromClient.getFrom()));
                    }else{
                        //心跳回复包
                        sendData(session,ProtocolFactory.createKeepAliveResponse(pFromClient.getFrom()));
                    }
                    break;
                case REQUEUST_CHAT_TEXT:
                    if(pFromClient.getTo()==0){

                    }else{
                        boolean sendOk=sendData(pFromClient);
                    }
                    break;
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
        return "";
    }

    protected int getNextUserId(LoginInfo loginInfo){
        return UserProcessor.nextUserId(loginInfo);
    }

    static boolean sendData(IoSession session,Protocol p) throws Exception{
        if(session.isConnected()){
            if(p!=null){
                byte[] res=p.toBytes();
                IoBuffer buf=IoBuffer.wrap(res);
                WriteFuture future=session.write(buf);
                future.awaitUninterruptibly(100L);
                if(future.isWritten()){
                    if(p.getFrom()==0){
                        return true;
                    }
                }
            }
        }else{

        }
        return false;
    }

    static boolean sendData(Protocol p) throws Exception {
        if(p!=null){
            return sendData(UserProcessor.getInstance().getSession(p.getTo()),p);
        }
        return false;
    }

}
