package com.earthgee.simplechat.net.entity;

/**
 * Created by earthgee on 17/3/24.
 */
public class ChatEntity {

    public static final int FROM_ME=0;
    public static final int FROM_OTHER=1;

    public static final int TYPE_TEXT=0;

    private int type;
    private int from;
    private String content;

    public ChatEntity(int type,int from,String content){
        this.type=type;
        this.from=from;
        this.content=content;
    }

    public int getType() {
        return type;
    }

    public int getFrom() {
        return from;
    }

    public String getContent() {
        return content;
    }
}
