package com.earthgee.simplechat.net;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.earthgee.simplechat.net.entity.ErrorInfo;
import com.earthgee.simplechat.net.entity.LoginResponse;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by earthgee on 17/2/9.
 */
public class LocalUDPReceiver {

    private static LocalUDPReceiver instance;
    private Thread thread=null;

    private static MessageHandler messageHandler;

    private static Context context;

    private LocalUDPReceiver(){
    }

    public static LocalUDPReceiver getInstance(Context contextReal){
        if(instance==null){
            instance=new LocalUDPReceiver();
            messageHandler=new MessageHandler();
            context=contextReal;
        }
        return instance;
    }

    public void startUp(){
        stop();

        try{
            thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        p2pListeningImpl();
                    }catch (Exception e){
                        //接收出现异常
                    }
                }
            });
            thread.start();
        }catch (Exception e){

        }
    }

    public void stop(){
        if(thread!=null){
            thread.interrupt();
            thread=null;
        }
    }

    private void p2pListeningImpl() throws IOException {
        while(true){
            byte[] data=new byte[1024];
            DatagramPacket packet=new DatagramPacket(data,data.length);
            DatagramSocket localUDPSocket=ConnectionManager.getInstance().
                    getLocalUDPProvider().getLocalUDPSocket();
            if((localUDPSocket==null)||(localUDPSocket.isClosed())){
                continue;
            }
            localUDPSocket.receive(packet);

            Message msg=Message.obtain();
            msg.obj=packet;
            messageHandler.sendMessage(msg);
        }
    }

    private static class MessageHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            DatagramPacket packet= (DatagramPacket) msg.obj;
            if(packet==null) return;

            try{
                Protocol pFromServer=ProtocolFactory.parse(packet.getData(),packet.getLength());

                if(pFromServer.isQos()){
                    if(Qos4ReceiveDaemon.getInstance().hasReceived(pFromServer.getFp())){
                        Qos4ReceiveDaemon.getInstance().addReceived(pFromServer);
                        sendReceivedBack(pFromServer);

                        return;
                    }

                    Qos4ReceiveDaemon.getInstance().addReceived(pFromServer);
                    sendReceivedBack(pFromServer);
                }

                switch (pFromServer.getType()){
                    case RESPONSE_LOGIN:
                        LoginResponse loginResponse=ProtocolFactory.parseLoginResponse(pFromServer.getContent());

                        if(loginResponse.getCode()==0){
                            //登录成功
                            ConnectionManager.getInstance().getClientCore().
                                    setCurrentUserId(loginResponse.getUserId());
                            Intent intent=new Intent("com.earthgee.login");
                            Bundle bundle=new Bundle();
                            bundle.putInt("userId",loginResponse.getUserId());
                            bundle.putInt("code",loginResponse.getCode());
                            intent.putExtras(bundle);
                            sendBroadCast(intent);
                            KeepAliveSevice.startPollingService(context);
                            Qos4ReceiveDaemon.getInstance().startUp();
                            QosSendDaemon.getInstance().startUp();
                            AutoReloginDameon.getInstance(context).stop();
                        }else{

                        }
                        break;
                    case REQUEUST_CHAT_TEXT:
                        //收到对方发来的消息
                        Intent intent=new Intent("com.earthgee.chat_text");
                        Bundle bundle=new Bundle();
                        bundle.putString("from",pFromServer.getFrom()+"");
                        bundle.putString("content", pFromServer.getContent());
                        intent.putExtras(bundle);
                        sendBroadCast(intent);
                        break;
                    case RESPONSE_KEEP_ALIVE:
                        KeepAliveSevice.updateKeepAliveResponseServerTimeStamp();
                        break;
                    case RESPONSE_QOS:
                        String fp=pFromServer.getContent();
                        QosSendDaemon.getInstance().remove(fp);
                        break;
                    case RESPONSE_ERROR:
                        ErrorInfo errorInfo=ProtocolFactory.parseErrorInfo(pFromServer.getContent());

                        if(errorInfo.getErrorcode()==301){
                            KeepAliveSevice.stopPollingService(context);
                            AutoReloginDameon.getInstance().start();
                        }
                        break;
                }
            }catch (Exception e){

            }
        }

        //发送qos回复包
        private void sendReceivedBack(final Protocol pFromServer){
            new RequestSender.SendTask(
                    ProtocolFactory.createReceivedBack
                            (pFromServer.getTo(),pFromServer.getFrom(),pFromServer.getFp())){

            }.execute();
        }
    }

    private static void sendBroadCast(Intent intent){
        context.sendBroadcast(intent);
    }

}
