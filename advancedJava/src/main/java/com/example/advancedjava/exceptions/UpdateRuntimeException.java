package com.example.advancedjava.exceptions;

public class UpdateRuntimeException extends RuntimeException {

    private String reqid;

    public UpdateRuntimeException(String message, String reqid) {
        super(message);
        this.reqid = reqid;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    
}
