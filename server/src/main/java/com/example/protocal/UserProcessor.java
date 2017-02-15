package com.example.protocal;

import org.apache.mina.core.session.IoSession;

import java.util.HashMap;

/**
 * Created by earthgee on 17/2/15.
 */
public class UserProcessor {

    private HashMap<Integer,IoSession> userSessions=new HashMap<>();

    public static int getUserIdFromSession(IoSession session){
        Object attr=null;
        if(session!=null){
            attr=session.getAttribute("__user_id__");
            if(attr!=null){
                return ((Integer)attr).intValue();
            }
        }
    }

}
