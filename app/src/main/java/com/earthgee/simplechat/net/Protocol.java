package com.earthgee.simplechat.net;

import com.earthgee.simplechat.util.CharsetUtil;
import com.google.gson.Gson;

/**
 * Created by earthgee on 17/2/6.
 */
public class Protocol {

    private ProtocolType type=null;
    private String content=null;
    private int from=-1;
    private int to=-1;
//    private boolean qos=false;

    public Protocol(ProtocolType type,String content,int from,int to){
        this.type=type;
        this.content=content;
        this.from=from;
        this.to=to;
    }

    public String toGsonString(){
        return new Gson().toJson(this);
    }

    public byte[] toBytes(){
        return CharsetUtil.getBytes(toGsonString());
    }


}
