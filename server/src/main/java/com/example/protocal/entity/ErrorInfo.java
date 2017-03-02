package com.example.protocal.entity;

/**
 * Created by earthgee on 17/3/2.
 */
public class ErrorInfo {

    int errorcode;

    public ErrorInfo(int errorcode){
        this.errorcode=errorcode;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }
}
