package com.example.advancedjava.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.example.advancedjava.helper.StatusWrapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class Response<T> {

   
    private StatusWrapper statusWrapper;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Response() {
    }

    private Response(String status, int status_code, String status_msg, String _reqid, String _server_ts, T data) {
        statusWrapper=new StatusWrapper(status, status_code, status_msg, _reqid, _server_ts);
        this.data = data;
    }

    @XmlElement
    @JsonProperty("status_details")
    public StatusWrapper getStatusWrapper() {
        return statusWrapper;
    }

    public void setStatusWrapper(StatusWrapper statusWrapper) {
        this.statusWrapper = statusWrapper;
    }
  

    @XmlElement
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    // Getters and setters

    public class Builder {

        private String status;
        private int status_code;
        private String status_msg;
        private String _reqid;
        private T data;

        public Builder() {}

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setStatus_code(int status_code) {
            this.status_code = status_code;
            return this;
        }

        public Builder setStatus_msg(String status_msg) {
            this.status_msg = status_msg;
            return this;
        }

        public Builder set_reqid(String _reqid) {
            this._reqid = _reqid;
            return this;
        }

        public Builder setData(T data) {
            this.data = data;
            return this;
        }

        public Response<T> build() {
            return new Response<>(status, status_code, status_msg, _reqid, LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString(), data);
        }
    }

  
}
