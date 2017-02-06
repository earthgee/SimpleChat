package com.earthgee.simplechat.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by earthgee on 17/2/6.
 */
public class CharsetUtil {

    public static final String ENCODE_CHARSET="UTF-8";
    public static final String DECODE_CHARSET="UTF-8";

    public static byte[] getBytes(String str){
        if(str!=null){
            try{
                return str.getBytes(ENCODE_CHARSET);
            }catch (UnsupportedEncodingException e){
                return str.getBytes();
            }
        }else{
            return new byte[0];
        }
    }

}
