package com.example.advancedjava.helper;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatusWrapper {

    private String status;
    private int status_code;
    private String status_msg;
    private String _reqid;
    private String _server_ts;

    public StatusWrapper() {
    }
    

    public StatusWrapper(String status, int status_code, String status_msg, String _reqid, String _server_ts) {
        this.status = status;
        this.status_code = status_code;
        this.status_msg = status_msg;
        this._reqid = _reqid;
        this._server_ts = _server_ts;
    }

    @XmlElement
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElement
    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    @XmlElement
    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    @XmlElement
    public String get_reqid() {
        return _reqid;
    }

    public void set_reqid(String _reqid) {
        this._reqid = _reqid;
    }

    @XmlElement
    public String get_server_ts() {
        return _server_ts;
    }

    public void set_server_ts(String _server_ts) {
        this._server_ts = _server_ts;
    }

    

    

    
}
