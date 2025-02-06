package com.example.advancedjava.dto;

import jakarta.validation.Valid;

public class Request<T extends Object>{

    @Valid
    private T data;
    private String _reqid;
    private String _client_ts;
    private String _client_type;

    public Request(@Valid T data, String _reqid, String _client_ts, String _client_type) {
        this.data = data;
        this._reqid = _reqid;
        this._client_ts = _client_ts;
        this._client_type = _client_type;
    } 

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String get_reqid() {
        return _reqid;
    }

    public void set_reqid(String _reqid) {
        this._reqid = _reqid;
    }

    public String get_client_ts() {
        return _client_ts;
    }

    public void set_client_ts(String _client_ts) {
        this._client_ts = _client_ts;
    }

    public String get_client_type() {
        return _client_type;
    }

    public void set_client_type(String _client_type) {
        this._client_type = _client_type;
    }

    

   
    

    
    
}
