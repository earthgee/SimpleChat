package com.earthgee.simplechat.net;

import android.os.AsyncTask;

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

    public void sendLoginRequest(String userName,String password,ISendListener sendListener){
        new LoginTask(userName, password, sendListener).execute();
    }

    private int sendLoginRequestReal(String userName,String passWord,ISendListener sendListener){
        //todo
        return -1;
    }

    private final class LoginTask extends AsyncTask<Object,Integer,Integer>{

        private String userName;
        private String password;
        private ISendListener sendListener;

        public LoginTask(String userName,String password,ISendListener sendListener){
            this.userName=userName;
            this.password=password;
            this.sendListener=sendListener;
        }

        @Override
        protected Integer doInBackground(Object... params) {
            int code=sendLoginRequestReal(userName,password,sendListener);
            return null;
        }



    }

}
