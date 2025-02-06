package com.example.advancedjava.exceptions;

import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class CustomMethodArgsNotValid extends MethodArgumentNotValidException{
    private String reqid;
    public CustomMethodArgsNotValid(MethodParameter parameter, BindingResult bindingResult,String reqid) {
        super(parameter, bindingResult);
        this.reqid=reqid;
    }
    public String getReqid() {
        return reqid;
    }
    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    
    
}
