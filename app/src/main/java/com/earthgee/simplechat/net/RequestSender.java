package com.earthgee.simplechat.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.earthgee.simplechat.util.ErrorCode;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by earthgee on 17/2/5.
 */
public class RequestSender {

    private static RequestSender instance;

    private RequestSender(){

    }

    public static RequestSender getInstance(){
        if(instance==null){
            instance=new RequestSender();
        }
        return instance;
    }

    public void sendLoginRequest(String userName,String password,Context context){
        new LoginTask(userName, password, context).execute();
    }

    private int sendLoginRequestReal(String userName,String passWord){
        byte[] b=ProtocolFactory.createLoginProtocol(userName,passWord).toBytes();
        int code=send(b, b.length);
        return code;
    }

    private int sendChatReqeuest(String toUserId,String content){
        byte[] bytes=ProtocolFactory.createChatProtocol(content,
                ConnectionManager.getInstance().getClientCore().getCurrentUserId(), toUserId).toBytes();
        return send(bytes,bytes.length);
    }

    public int sendReloginRequest(int lastUserId){
        return -1;
    }

    int sendKeepAlive(){
        byte[] bytes=ProtocolFactory.createKeepAlive
                (ConnectionManager.getInstance().getClientCore().getCurrentUserId())
                .toBytes();
        return send(bytes,bytes.length);
    }

    public int sendDefaultData(Protocol protocol){
        byte[] bytes=protocol.toBytes();
        int code=send(bytes,bytes.length);
        if(protocol.isQos()&&!QosSendDaemon.getInstance().exist(protocol.getFp())){
            QosSendDaemon.getInstance().put(protocol);
        }
        return code;
    }

    private int send(byte[] fullProtocolBytes,int dataLen){
        if(!ConnectionManager.getInstance().isNetAvaiable()){
            return ErrorCode.LOCAL_NET_NO_CONNECTED;
        }

        DatagramSocket ds=ConnectionManager.getInstance().
                getLocalUDPProvider().getLocalUDPSocket();
        if(ds!=null&&!ds.isConnected()){
            try{
                ds.connect(InetAddress.getByName(Config.serverIP),Config.serverUdpPort);
            }catch (Exception e){
                return ErrorCode.BAD_CONNECT_TO_SERVER;
            }
        }

        boolean sendSuccess=true;
        try{
            DatagramPacket packet=new DatagramPacket(fullProtocolBytes,dataLen);
            ds.send(packet);
        }catch (Exception e){
            sendSuccess=false;
        }

        return sendSuccess?ErrorCode.COMMON_CODE_OK:ErrorCode.SEND_PACKET_FAIL;
    }

    private final class LoginTask extends AsyncTask<Object,Integer,Integer>{

        private String userName;
        private String password;
        private Context context;

        public LoginTask(String userName,String password,Context context){
            this.userName=userName;
            this.password=password;
            this.context=context;
        }

        @Override
        protected Integer doInBackground(Object... params) {
            int code=sendLoginRequestReal(userName,password);
            return Integer.valueOf(code);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer==0){
                LocalUDPReceiver.getInstance(context).startUp();
            }else{
                //发送失败
            }
        }
    }

    public static class SendTask extends AsyncTask<Object,Integer,Integer>{

        private String userId;
        private String content;
        private Context context;
        private Protocol protocol;

        public SendTask(String userId,String content,Context context){
            this.userId=userId;
            this.content=content;
            this.context=context;
        }

        public SendTask(String toUserId,String content,boolean Qos){
            this(ProtocolFactory.createChatProtocol(content,
                    ConnectionManager.getInstance().getClientCore().getCurrentUserId(),toUserId));
        }

        public SendTask(Protocol p){
            this.protocol=p;
        }

        @Override
        protected Integer doInBackground(Object... params) {
            int code=0;
            if(protocol==null){
                code=RequestSender.getInstance().
                        sendChatReqeuest(userId, content);
            }else{
                code=RequestSender.getInstance().sendDefaultData(protocol);
            }

            return code;
        }

    }

}
