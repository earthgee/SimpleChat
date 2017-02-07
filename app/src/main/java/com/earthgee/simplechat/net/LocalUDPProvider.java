package com.earthgee.simplechat.net;

import java.net.DatagramSocket;

/**
 * Created by earthgee on 17/2/7.
 */
public class LocalUDPProvider {

    private DatagramSocket localUDPSocket=null;

    public LocalUDPProvider(){

    }

    private boolean isLocalUDPSocketReady(){
        return (localUDPSocket)!=null&&(!localUDPSocket.isClosed());
    }

    public DatagramSocket getLocalUDPSocket(){
        if(isLocalUDPSocketReady()){
            return localUDPSocket;
        }else{
            return resetLocalUDPSocket();
        }
    }

    private DatagramSocket resetLocalUDPSocket(){
        try{
            closeLocalUDPSocket();
            this.localUDPSocket=Config.localUdpPort==0?
                    new DatagramSocket():new DatagramSocket(Config.localUdpPort);
            return localUDPSocket;
        }catch (Exception e){
            closeLocalUDPSocket();
        }

        return null;
    }

    public void closeLocalUDPSocket(){
        try{
            if(localUDPSocket!=null){
                localUDPSocket.close();
                localUDPSocket=null;
            }
        }catch (Exception e){

        }
    }

}
