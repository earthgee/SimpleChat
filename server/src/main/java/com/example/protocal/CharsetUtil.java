package com.example.protocal;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by earthgee on 17/2/6.
 */
public class CharsetUtil {
    public static final CharsetDecoder decoder=Charset.forName("UTF-8").newDecoder();
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

    public static String getString(byte[] b,int len){
        try{
            return new String(b,0,len,DECODE_CHARSET);
        }catch (UnsupportedEncodingException e){
            return new String(b,0,len);
        }
    }

}
