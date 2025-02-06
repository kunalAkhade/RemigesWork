package com.example.advancedjava.exceptions;

public class InvalidProperty extends RuntimeException {
    private String reqid;
    public InvalidProperty(String message,String reqid) {
        super(message);
        this.reqid=reqid;
    }
    public String getReqid() {
        return reqid;
    }
    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    
}
