package com.example.protocal;

import com.example.protocal.entity.LoginInfo;

import org.apache.mina.core.session.IoSession;

import java.util.HashMap;

/**
 * Created by earthgee on 17/2/15.
 */
public class UserProcessor {

    public static final String USER_ID_IN_SESSION_ATTRIBUTE="__user_id__";
    public static final String LOGIN_NAME_IN_SESSION_ATTRIBUTE="__login_name__";

    private static int __id=10000;

    //内存中保存用户信息
    private HashMap<Integer,IoSession> userSessions=new HashMap<>();
    private HashMap<Integer,String> userNames=new HashMap<>();

    public static int getUserIdFromSession(IoSession session){
        Object attr=null;
        if(session!=null){
            attr=session.getAttribute("__user_id__");
            if(attr!=null){
                return ((Integer)attr).intValue();
            }
        }
        return -1;
    }

    public static boolean isLogined(IoSession session){
        return (session!=null)&&(getUserIdFromSession(session)!=-1);
    }

    public static int nextUserId(LoginInfo loginInfo){
        return ++__id;
    }

    public void putUser(int userId,IoSession session,String userName){
        userSessions.put(userId,session);
        if(userName!=null){
            userNames.put(userId,userName);
        }
    }

    public IoSession getSession(int userId){
        return userSessions.get(userId);
    }

    private static UserProcessor instance;

    private UserProcessor(){}

    public static UserProcessor getInstance(){
        if(instance==null){
            instance=new UserProcessor();
        }
        return instance;
    }

}
